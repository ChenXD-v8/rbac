package com.jeasyuicn.rbac.interceptor;

import com.jeasyuicn.rbac.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Component
public class RightsInterceptor implements HandlerInterceptor {

    @Value("${system.super.user.id}")
    private Long superId;
    //Spring 路径匹配
    private PathMatcher matcher =new AntPathMatcher();
    @Override
    public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute("user");
        if(user.getId()!=superId){
            Set<String> urls=(Set<String>) session.getAttribute("urls");
            //获取当前访问路径
            String path=request.getServletPath();
            for (String url : urls) {
                if (url!=null&&matcher.match(url, path)) {
//                    System.out.println("url:"+url);
//                    System.out.println("当前访问路径："+path);
                    //能匹配到当前的URL，表示已授权
                    return true;
                }
            }
            if("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))){
                response.sendError(403);
            }else{
                //非ajax请求，需要提示
                response.sendRedirect(request.getContextPath()+"/reject");
            }
            return false;
        }
        return true;
    }
}
