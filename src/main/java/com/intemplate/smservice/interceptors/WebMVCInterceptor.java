package com.intemplate.smservice.interceptors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMVCInterceptor implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/register") //用户注册
                .excludePathPatterns("/login"); //用户登录
    }

    /**
     * @return globalInterceptor
     */
    @Bean
    public GlobalInterceptor globalInterceptor() {
        return new GlobalInterceptor();
    }
}
