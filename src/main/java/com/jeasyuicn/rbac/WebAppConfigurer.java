package com.jeasyuicn.rbac;

import com.jeasyuicn.rbac.interceptor.LoginInterceptor;
import com.jeasyuicn.rbac.interceptor.RightsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    RightsInterceptor rightsInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration registration= registry.addInterceptor(new LoginInterceptor());
        //此处表示需要拦截程序的所有请求
        registration.addPathPatterns("/**");
        //不拦截的请求
        registration.excludePathPatterns("/","/login","/error","/css/**","/menus","/js/**","/easyui/**","/lib/**","/font-awesome/**");

        //请求的权限呢过滤器
        InterceptorRegistration  rightsRegistration= registry.addInterceptor( rightsInterceptor);
        rightsRegistration.addPathPatterns("/**");
        rightsRegistration.excludePathPatterns("/","/login","/logout","/error","/menus","/css/**","/js/**","/easyui/**","/lib/**","/font-awesome/**");
    }

    /**
     * 哎 没什么乱用
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/static/**").addResourceLocations( "/static/");
        System.out.println("到我这了");
    }
}
