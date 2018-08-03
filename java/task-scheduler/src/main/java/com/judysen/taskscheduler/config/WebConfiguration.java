package com.judysen.taskscheduler.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.judysen.taskscheduler.controller.interceptor.CookieInterceptor;
import com.judysen.taskscheduler.controller.interceptor.PermissionInterceptor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration permissionAddInterceptor = registry.addInterceptor(getPermissionInterceptor());
//        permissionAddInterceptor.addPathPatterns("/**").excludePathPatterns("/adminlte/**","/js/**","/plugins/**","/error","/toLogin");

        InterceptorRegistration cookieAddInterceptor = registry.addInterceptor(getCookieInterceptor());
        cookieAddInterceptor.addPathPatterns("/**").excludePathPatterns("/adminlte/**","/js/**","/plugins/**","/error");
    }
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }
    /**
     * 配置FastJson为方式一
     * @return*/
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//
//        /*
//         * 1、需要先定义一个convert转换消息的对象 2、添加fastJson的配置信息，比如：是否要格式化返回json数据 3、在convert中添加配置信息
//         * 4、将convert添加到converters当中
//         *
//         */
//        // 1、需要先定义一个·convert转换消息的对象；
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        // 2、添加fastjson的配置信息，比如 是否要格式化返回json数据
////        FastJsonConfig fastJsonConfig = new FastJsonConfig();
////        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
////        // 3、在convert中添加配置信息.
////        fastConverter.setFastJsonConfig(fastJsonConfig);
//        // 4、将convert添加到converters当中.
//        converters.add(fastConverter);
//        super.configureMessageConverters(converters);
//    }
    @Bean
    public CookieInterceptor getCookieInterceptor() {
        return new CookieInterceptor();
    }

    @Bean
    public PermissionInterceptor getPermissionInterceptor(){
        return  new PermissionInterceptor();
    }
    /**
     * 配置FastJson方式二
     * @return  HttpMessageConverters
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 4.将converter赋值给HttpMessageConverter
        HttpMessageConverter<?> converter = fastConverter;
        // 5.返回HttpMessageConverters对象
        return new HttpMessageConverters(converter);
    }
}
