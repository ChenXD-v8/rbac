    $(function(){
    var roleGrid=$('#roleGrid');
    var roleGridAction=$('#roleGridAction');
    var rolePermissionPanel=$('#rolePermissionPanel');
    var rolePermissionTree=$("#rolePermissionTree");
    var currentRow;
    roleGrid.datagrid({
    fit:true,
    border:false,
    url:'/system/role/list',
    singleSelect:true,
    columns:[[
        {field:'id',hidden:true},
        {title:'名称',field:'roleName',width:180},
        {field:'roleKey',title:'标识',width:120,align:'right'},
        {field:'description',title:'描述',width:80},
        {field:'enable',title:'状态',width:80,align:'center',formatter:function(val){
           return val ?"可用":"禁用";}},
         {field:'cz',title:'操作',width:100,align:'center',formatter:function(val,row){
                return roleGridAction.children("a.actions").attr('data-id',row.id).end() .html();
                }
         }
    ]],
    toolbar:"#roleGridToolbar",
    onSelect:function (index,row) {
        currentRow=row;
        rolePermissionPanel.panel("setTitle","为["+row.roleName+"]分配权限" );
        //取消以前所有的选中的项目
        $.each(rolePermissionTree.tree("getChecked"),function () {
            rolePermissionTree.tree("uncheck",this.target)
        });
        //加载当前选中角色的已有权限
        $.get("/system/role/permission/"+row.id,function (data) {
            if(data){
                $.each(data,function(){
                    var node=rolePermissionTree.tree("find",this.id);
                    //只有当前权限是树的叶子节点才执行节点的check方法进行选择
                    if(rolePermissionTree.tree("isLeaf",node.target)){
                        rolePermissionTree.tree("check",node.target);
                    }
                });
            }
        });
    }
    });
    var gridPanel=roleGrid.datagrid("getPanel");
    //给操作按钮绑定事件
    gridPanel.on("click","a.edit",function(){
     var id=this.dataset.id;
        formDialog(id);
    }).on("click","a.delete",function(){
           var id=this.dataset.id;
         $.messager.confirm("提示","是否删除",function(r){
         if(r){
         $.get("/system/role/delete?id="+id).success(function(){
           roleGrid.datagrid("reload");
         });
         }
         })
          }).on("click","a.create",function () {
        formDialog();
    });
    /**
    *表单窗口
    */
    function formDialog(id){
      var dialog=$("<div/>").dialog({
        title:(id?'编辑':'创建')+'角色',
        href:'/system/role/'+(id?'load?id='+id :'form'),
        width:380,
        height:250,
        onClose:function(){
        //销毁dialog
         $(this).dialog("destroy");
        },
        buttons:[
        {
          text:'保存',
          handler:function(){
          var roleForm=$("#roleForm");
          if(roleForm.form("validate")){
           $.post("/system/role/"+(id?"update":"save"),
               roleForm.serialize()).success(function(ms){
               if(!ms.success){
                   alert(ms.msg);
               }
           roleGrid.datagrid("reload");
           dialog.dialog('close');
           });
          }
          }
        }]
      });
    }


    rolePermissionTree.tree({
        url:'/system/role/permissionTree',
        checkbox:true
    });
    //权限保存的按钮事件
        $('#rolePermissionSave').on('click',function () {
            if(currentRow){
                //获取当前树形节点勾选的和实心的节点
                var nodes=rolePermissionTree.tree('getChecked',["checked","indeterminate"]);
                var permissionIds=[];
                $.each(nodes,function () {
                    permissionIds.push(this.id);
                });
                var params="roleId="+currentRow.id+"&permissionId="+permissionIds.join("&permissionId=");
                $.post("/system/role/permissionSave",params,function (resp) {
                        if(resp.success){
                        $.messager.alert("提示","授权成功");
                    }
                });
            }

        });
    });