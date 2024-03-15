package com.prgrms.zzalmyu.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "https://zzalmyu.site", "https://www.zzalmyu.site")
                .allowedMethods("GET", "POST", "DELETE", "PATCH")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Authorization-refresh")
                .maxAge(3000);
    }
}
