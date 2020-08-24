package com.jeasyuicn.rbac.controller.system;

import com.jeasyuicn.rbac.common.JsonResult;
import com.jeasyuicn.rbac.model.dao.RoleDao;
import com.jeasyuicn.rbac.model.dao.UserDao;
import com.jeasyuicn.rbac.model.entity.Role;
import com.jeasyuicn.rbac.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/user")
@Slf4j
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @RequestMapping
    public void index() {

    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int rows) {
        //原先PageRequest()方法失效l
        Pageable pb = PageRequest.of(page - 1, rows, Sort.Direction.DESC, "id");
        Page<User> pageData = userDao.findAll(pb);

        Map<String, Object> data = new HashMap<>();
        //总记录数
        data.put("total", pageData.getTotalElements());
        //当前页数据
        data.put("rows", pageData.getContent());
        return data;
    }

    @RequestMapping({"/form", "/load"})
    public String form(Long id, Model model) {
        if (id != null) {
            User user = userDao.findById(id).orElse(null);
            model.addAttribute("user", user);
        }
        List<Role> roles=roleDao.findAllByEnable(true);
        model.addAttribute("roles",roles );
        String roleIds="";
        for (Role role : roles) {
            System.out.println("roles:  "+role.getId());
            roleIds+=role.getId()+",";
        }
        roleIds.substring(0,roleIds.length()-1);
        System.out.println("roles:  "+roleIds);
        model.addAttribute("roleIds",roleIds);
        return "system/user/form";
    }

    @RequestMapping({"/save", "/update"})
    @ResponseBody
    @Transactional
    public JsonResult form(@Valid User user, BindingResult br) {
        if (!br.hasErrors()) {
            if (user.getId() == null) {
                //md5加密密码
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            } else {
                User org = userDao.findById(user.getId()).orElse(null);
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    user.setPassword(DigestUtils.md5Hex(user.getPassword()));
                } else {
                    user.setPassword(org.getPassword());
                }
            }
            for(Role role:user.getRoles()){
                System.out.println("----"+role.getId()+"---"+role.getRoleName());
            }
            userDao.save(user);
            return JsonResult.success();
        } else {
            return JsonResult.error("校验不通过");
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    @Transactional
    public JsonResult delete(Long id) {
        User user = userDao.findById(id).orElse(null);
        if (user != null) {
            userDao.delete(user);
            return JsonResult.success();
        } else {
            return JsonResult.error("校验不通过");
        }
    }

    @RequestMapping("/roleTree")
    @ResponseBody
    public Iterable permissionTree() {
        return roleDao.findAll();
    }

    @PostMapping("/check")
    @ResponseBody
    public String check(String account) {
        if (userDao.countByAccount(account) == 0) {
            return "true";
        }
        return "false";
    }
}
