package com.jeasyuicn.rbac.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        //判断session中是否有用户信息
        if(session.getAttribute("user")==null){
            //ajax请求，需要提示
            if("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))){
                response.sendError(401);
            }else{
                response.sendRedirect("/");
            }
            return false;
        }
        return true;
    }
}
