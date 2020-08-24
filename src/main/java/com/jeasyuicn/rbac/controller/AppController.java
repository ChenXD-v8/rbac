package com.jeasyuicn.rbac.controller;

import com.jeasyuicn.rbac.common.Menu;
import com.jeasyuicn.rbac.model.dao.PermissionDao;
import com.jeasyuicn.rbac.model.dao.UserDao;
import com.jeasyuicn.rbac.model.entity.Permission;
import com.jeasyuicn.rbac.model.entity.Role;
import com.jeasyuicn.rbac.model.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;
    @Value("${system.super.user.id}")
    private Long superId;
    @RequestMapping
    public String Index(@SessionAttribute(value = "user",required = false)User user) {
        //此处表示未登录
        if(user==null){
            return "login";
        }
        //已登录
        return "Index";
    }
    @PostMapping("/login")
    public String login(@RequestParam String account, @RequestParam String password, HttpSession session, RedirectAttributes rda){
        User user=userDao.findFirstByAccount(account);
        Set<Permission> permissions;
        //判断账号是否可用
        if(user!=null&&user.getEnable()){
            //判断密码是否匹配
            if(user.getPassword().equals(DigestUtils.md5Hex(password))){
                //判断是否是超级管理员
                if(Objects.equals(user.getId(),superId)){
                    permissions=permissionDao.findAllByEnableOrderByWeightDesc(true);
                }else{
                    //获取用户菜单
                    Set<Role> roles=user.getRoles();
                    permissions=new HashSet<>();
                    roles.forEach(role -> permissions.addAll(role.getPermissions()));
                }
                //存储菜单
                TreeSet<Permission> menus=new TreeSet<>((o1, o2) -> {
                    if(Objects.equals(o1.getWeight(),o2.getWeight())){
                        return -1;
                    }
                    return o1.getWeight()-o2.getWeight();
                });

                //存储权限key
                Set<String> keys=new HashSet<>();
                //所有有权限访问的请求
                Set<String> urls=new HashSet<>();
                   permissions.forEach(permission -> {
                       if(permission.getEnable()){
                           if(permission.getType().equals(Permission.Type.MENU.name())){
                               //是菜单
                               menus.add(permission);
                               urls.add(permission.getPath());
                           }
                           keys.add(permission.getPermissionKey());
                           urls.addAll(Arrays.asList(permission.getResource().split(",")));
                       }
                   });
                //树形数据转换
                List<Menu> menuList=new ArrayList<>();
                menus.forEach(permission -> {
                    Menu m=new Menu();
                    m.setId(permission.getId());
                    m.setText(permission.getName());
                    m.setParentId(permission.getParent()==null?null:permission.getParent().getId());
                    m.setHref(permission.getPath());
                    menuList.add(m);
                });
                session.setAttribute("user",user);
                session.setAttribute("menus",menuList);
                session.setAttribute("keys",keys);
                session.setAttribute("urls",urls);
            }else{
                rda.addFlashAttribute("error","账号和密码不匹配");
            }
        }else{
            rda.addFlashAttribute("error","账号不可用");
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
    @RequestMapping("/menus")
    @ResponseBody
    public List<Menu> menu(@SessionAttribute("menus") List<Menu>  menuList){
        List<Menu> menus=menuList;
       getChildren(menuList,menus);
       List<Menu> resultMenu=new ArrayList<>();
        menus.forEach(menu->resultMenu.add(menu));
        //防止界面刷新 菜单显示不全
        resultMenu.removeIf(menu->menu.getParentId()!=null);
        return resultMenu;
    }

    /**
     * 将list递归生成父子节点的形式
     * @param menuList
     */
    private void getChildren(@SessionAttribute("menus")List<Menu> menuList,List<Menu> menuList2)
    {
        menuList2.forEach(menu -> {
            List<Menu> children=new ArrayList<>();
                menuList.forEach(node->{
                    if(node.getParentId()!=null&&node.getParentId().equals(menu.getId())) {
                        children.add(node);
                    }
                });
                if(!children.isEmpty()){
                    getChildren(menuList,children);
                    menu.setChildren(children);
                }
                else{
                    menu.setChildren(null);
                }
        });
    }


    @RequestMapping("/reject")
    public String reject(){
        return "reject";
    }

}
