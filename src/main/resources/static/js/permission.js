    $(function(){
    var permissionGrid=$('#permissionGrid');
    var permissionGirdAction=$('#permissionGirdAction');
    var types={
       MENU:"菜单",
       FUNCTION:"功能",
       BLOCK:"区域"
    };

    permissionGrid.treegrid({
    fit:true,
    border:false,
    url:'/system/permission/list',
    idField:'id',
    treeField:'name',
    columns:[[
        {title:'名称',field:'name',width:180},
        {field:'permissionKey',title:'标识',width:120,align:'right'},
        {field:'type',title:'类型',width:80,formatter:function(val){
        return types[val];
        }},
        {field:'path',title:'路径',width:180},
        {field:'resource',title:'资源',width:300},
        {field:'weight',title:'权重',width:80},
        {field:'description',title:'描述',width:80},
        {field:'enable',title:'状态',width:80,align:'center',formatter:function(val){
           return val ?"可用":"禁用";}},
         {field:'cz',title:'操作',width:100,align:'center',formatter:function(val,row){
                return permissionGirdAction.children("a.actions").attr('data-id',row.id).end().html();
                }
         }
    ]],
    toolbar:'#permissionGridToolbar'
    });
    var gridPanel=permissionGrid.treegrid("getPanel");
    //给操作按钮绑定事件
    gridPanel.on("click","a.edit",function(){
     var id=this.dataset.id;
        formDialog(id);
    }).on("click","a.delete",function(){
           var id=this.dataset.id;
         $.messager.confirm("提示","是否删除",function(r){
         if(r){
         $.get("/system/permission/delete?id="+id).success(function(){
           permissionGrid.treegrid("reload");
         });
         }
         })
          }).on("click","a.create",function(){
              formDialog();
    });
    /**
    *表单窗口
    */
    function formDialog(id){
      var dialog=$("<div/>").dialog({
        title:(id?'编辑':'创建')+'权限',
        href:'/system/permission/'+(id?'load?id='+id :'form'),
        width:380,
        height:440,
        onClose:function(){
        //销毁dialog
         $(this).dialog("destroy");
        },
        buttons:[
        {
          text:'保存',
          handler:function(){
          var permissionForm=$("#permissionForm");
          if(permissionForm.form("validate")){
           $.post("/system/permission/"+(id?"update":"save"),
               permissionForm.serialize()).success(function(){
           permissionGrid.treegrid("reload");
           dialog.dialog('close');
           });
          }
          }
        }]
      });
    }
    });