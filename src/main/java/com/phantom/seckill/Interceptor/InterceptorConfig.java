package com.phantom.seckill.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AccessInterceptor accessInterceptor;
    @Autowired
    private TokenInterceptor tokenInterceptor;

    /**
     * 增加拦截
     */
    public void addInterceptors(InterceptorRegistry registry) {
        //访问其他页面都要验证
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/goods/**","/order/**","/seckill/**","/code/**").excludePathPatterns("/login","/static/**");
        registry.addInterceptor(accessInterceptor).addPathPatterns("/code/**","/seckill/**").excludePathPatterns("/code/imgvrifyControllerDefaultKaptcha","/seckill/result");

    }
}
