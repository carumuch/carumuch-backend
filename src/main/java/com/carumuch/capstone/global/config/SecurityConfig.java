package com.carumuch.capstone.global.config;

import com.carumuch.capstone.domain.auth.filter.CustomLogoutFilter;
import com.carumuch.capstone.domain.auth.filter.CustomUserLoginFilter;
import com.carumuch.capstone.domain.auth.filter.CustomUsernamePasswordAuthenticationFilter;
import com.carumuch.capstone.domain.auth.filter.JwtAuthenticationFilter;
import com.carumuch.capstone.domain.auth.handler.CustomOAuth2SuccessHandler;
import com.carumuch.capstone.domain.auth.jwt.*;
import com.carumuch.capstone.domain.auth.service.CustomOAuth2UserService;
import com.carumuch.capstone.domain.auth.handler.CustomAccessDeniedHandler;
import com.carumuch.capstone.domain.auth.handler.CustomAuthenticationEntryPoint;
import com.carumuch.capstone.domain.auth.handler.CustomOAuth2AuthenticationFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

import static com.carumuch.capstone.global.constants.CorsConstant.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList(ALLOWED_ORIGINS));
                    configuration.setAllowedMethods(Collections.singletonList(ALLOWED_METHODS));
                    configuration.setAllowCredentials(ALLOWED_CREDENTIALS);
                    configuration.setAllowedHeaders(Collections.singletonList(ALLOWED_HEADERS));
                    configuration.setMaxAge(MAX_AGE);
                    configuration.setExposedHeaders(Collections.singletonList(HttpHeaders.SET_COOKIE));
                    configuration.setExposedHeaders(Collections.singletonList(HttpHeaders.AUTHORIZATION));
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_PATTERNS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/signup", "/login", "/reissue").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/check-email/**", "/users/check-login-id/**").permitAll()
                        .requestMatchers("/password/reset/**", "/find-id").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .addFilterAt(new CustomUserLoginFilter(authenticationManager, tokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthenticationFilter(tokenProvider), CustomUsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(tokenProvider, objectMapper), LogoutFilter.class)
                .oauth2Login((oauth2) -> oauth2.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService)))
                        .successHandler(new CustomOAuth2SuccessHandler(tokenProvider))
                        .failureHandler(new CustomOAuth2AuthenticationFailureHandler()))
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper)));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] SWAGGER_PATTERNS = {
            "/api-docs/**" ,
            "/v3/**",
            "/swagger-ui/**"
    };
}
