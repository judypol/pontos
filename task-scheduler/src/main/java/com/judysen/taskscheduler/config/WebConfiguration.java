package com.judysen.taskscheduler.config;

import com.judysen.taskscheduler.controller.interceptor.CookieInterceptor;
import com.judysen.taskscheduler.controller.interceptor.PermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration permissionAddInterceptor = registry.addInterceptor(getPermissionInterceptor());
        permissionAddInterceptor.addPathPatterns("/**").excludePathPatterns("/adminlte/**","/js/**","/plugins/**","/error","/toLogin");

        InterceptorRegistration cookieAddInterceptor = registry.addInterceptor(getCookieInterceptor());
        cookieAddInterceptor.addPathPatterns("/**").excludePathPatterns("/adminlte/**","/js/**","/plugins/**","/error");
    }

    @Bean
    public CookieInterceptor getCookieInterceptor() {
        return new CookieInterceptor();
    }

    @Bean
    public PermissionInterceptor getPermissionInterceptor(){
        return  new PermissionInterceptor();
    }
}
