package com.carumuch.capstone.common.infrastructure.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // private final UserRepository userRepository; //TODO: USER 적용 후 주석 제거 진행

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/static/swagger-ui/");
    }

    // @Override
    // public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    //     resolvers.add(new AuthUserResolver(userRepository));
    // }
}
