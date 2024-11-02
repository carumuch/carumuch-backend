package com.carumuch.capstone.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    private final String ALLOWED_ORIGINS;

    public CorsMvcConfig(@Value("${cors.allow.origins}") String ALLOWED_ORIGINS) {
        this.ALLOWED_ORIGINS = ALLOWED_ORIGINS;
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins(ALLOWED_ORIGINS);
    }
}
