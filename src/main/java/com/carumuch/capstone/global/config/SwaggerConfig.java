package com.carumuch.capstone.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    /**
     * swagger 설정
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                        .addList("Access Token ")
                        .addList("Refresh Token"))
                .components(new Components()
                        .addSecuritySchemes("Access Token ", createAPIKeyScheme())
                        .addSecuritySchemes("Refresh Token", createRTKeyScheme()))
                .info(new Info()
                        .title("Capstone")
                        .version("v2.0.2")
                        .description("**인공지능을 통한 `차량 사고 분석` 및 `입찰` 시스템**"));

    }

    /**
     * Access Token header 설정
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * Refresh Token header 설정
     */
    private SecurityScheme createRTKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("refresh-token");
    }
}
