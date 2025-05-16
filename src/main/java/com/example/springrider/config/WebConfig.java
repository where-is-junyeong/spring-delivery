package com.example.springrider.config;

import com.example.springrider.config.filter.SessionLoginCheckFilter;
import com.example.springrider.config.interceptor.StoreOwnerInterceptor;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionLoginCheckFilter loginCheckFilter;
    private final StoreOwnerInterceptor storeOwnerInterceptor;

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(loginCheckFilter);
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1); // 필터 순서 지정
        return bean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(storeOwnerInterceptor)
            .addPathPatterns("/api/owners/**"); // 사장 인증 경로
    }
}
