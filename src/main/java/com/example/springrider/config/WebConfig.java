package com.example.springrider.config;

import com.example.springrider.config.interceptor.StoreOwnerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StoreOwnerInterceptor storeOwnerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(storeOwnerInterceptor)
            .addPathPatterns("/api/owners/**"); // 사장 인증 경로
    }
}
