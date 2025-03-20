package com.ncf.apollodemo.config;

import com.ncf.apollodemo.handler.ApolloClientInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    // 确保拦截器正确注入
    @Autowired
    private ApolloClientInterceptor apolloClientInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apolloClientInterceptor)
            .addPathPatterns("/apollo/**")
            .excludePathPatterns("/error") // 排除Spring Boot错误端点
            .order(0); // 设置为最高优先级
    }
}