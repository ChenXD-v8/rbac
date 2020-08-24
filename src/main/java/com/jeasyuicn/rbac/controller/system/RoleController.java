package com.jeasyuicn.rbac.controller.system;

import com.jeasyuicn.rbac.common.JsonResult;
import com.jeasyuicn.rbac.model.dao.PermissionDao;
import com.jeasyuicn.rbac.model.dao.RoleDao;
import com.jeasyuicn.rbac.model.entity.Permission;
import com.jeasyuicn.rbac.model.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/system/role")
@Slf4j
public class RoleController {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @RequestMapping
    public void index(){

    }

    @RequestMapping("/list")
    @ResponseBody
    public Iterable list(){
        return roleDao.findAll();
    }

    @RequestMapping({"/form","/load"})
    public String form(Long id, Model model){

        if(id!=null){
            Role role=roleDao.findById(id).orElse(null);
            model.addAttribute("role",role);
        }
        return "system/role/form";
    }

    @RequestMapping({"/save","/update"})
    @ResponseBody
    @Transactional
    public JsonResult form(@Valid Role role, BindingResult br){
        if(!br.hasErrors()){
            if(role.getId()==null){
            if(roleDao.findFirstByRoleName(role.getRoleName())!=null){
                    return JsonResult.error("该角色已被创建");
                }
            }
            roleDao.save(role);
            return JsonResult.success();
        }
        else{
            return JsonResult.error("校验不通过");
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    @Transactional
    public JsonResult delete(Long id){
        Role role=roleDao.findById(id).orElse(null);
        if(role!=null){
            roleDao.delete(role);
            return JsonResult.success();
        }
        else{
            return JsonResult.error("校验不通过");
        }
    }

    @RequestMapping("/permissionTree")
    @ResponseBody
    public List<Permission> permissionTree(){
        return permissionDao.findAllByParentIsNull();
    }

    /**
     * 获取角色对应的权限列表
     * @param id
     * @return
     */
    @RequestMapping("permission/{id}")
    @ResponseBody
    public Set<Permission> permission(@PathVariable("id")Long id){
        Role role=roleDao.findById(id).orElse(null);
        return role.getPermissions();
    }

    @RequestMapping("/permissionSave")
    @Transactional
    @ResponseBody
    public JsonResult permissionSave(Long roleId,Long[] permissionId){
        Role role=roleDao.findById(roleId).orElse(null);
        //先清楚已有角色
        role.getPermissions().clear();
        for (Long pid : permissionId) {
            role.getPermissions().add(permissionDao.findById(pid).orElse(null));
        }
        roleDao.save(role);
        return JsonResult.success("授权成功");
    }
}
