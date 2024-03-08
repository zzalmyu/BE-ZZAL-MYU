package com.prgrms.zzalmyu.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:8080", "http://localhost:80")
                .allowedMethods("GET", "POST", "DELETE", "PATCH")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Authorization-refresh")
                .maxAge(3000);
    }
}
