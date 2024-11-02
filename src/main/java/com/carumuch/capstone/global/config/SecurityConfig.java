package com.carumuch.capstone.global.config;

import com.carumuch.capstone.auth.jwt.*;
import com.carumuch.capstone.auth.service.AuthService;
import com.carumuch.capstone.auth.service.CustomOAuth2UserService;
import com.carumuch.capstone.global.common.exception.CustomAccessDeniedHandler;
import com.carumuch.capstone.global.common.exception.CustomAuthenticationEntryPoint;
import com.carumuch.capstone.global.common.exception.CustomOAuth2AuthenticationFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler;

    private final String ALLOWED_ORIGINS;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          JwtTokenProvider jwtTokenProvider,
                          AuthService authService,
                          ObjectMapper objectMapper,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CustomAccessDeniedHandler customAccessDeniedHandler,
                          CustomOAuth2UserService customOAuth2UserService,
                          CustomOAuth2SuccessHandler customOAuth2SuccessHandler,
                          CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler,
                          @Value("${cors.allow.origins}") String ALLOWED_ORIGINS) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
        this.objectMapper = objectMapper;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.customOAuth2AuthenticationFailureHandler = customOAuth2AuthenticationFailureHandler;
        this.ALLOWED_ORIGINS = ALLOWED_ORIGINS;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList(ALLOWED_ORIGINS));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("authorization"));

                        return configuration;
                    }
                }));
        http
                .csrf((auth) -> auth.disable());
        http
                .formLogin((auth) -> auth.disable());
        http
                .httpBasic((auth) -> auth.disable());
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api-docs/**" ,
                                "/v3/**",
                                "/swagger-ui/**", // 스웨거
                                "/login", // 로그인
                                "/users/signup", // 회원가입
                                "/users/check-email/**",
                                "/users/check-login-id/**",
                                "/password/reset/**", // 비밀번호 찾기
                                "/find-id" // 아이디 찾기
                        ).permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated());
        /* 토큰 검증 필터 */
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        CustomUsernamePasswordAuthenticationFilter.class);
        /* 소셜 로그인 */
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2AuthenticationFailureHandler));
        /* 로그인 필터 */
        http
                .addFilterAt(new CustomUsernamePasswordAuthenticationFilter(
                        authenticationManager(authenticationConfiguration), authService, objectMapper),
                        UsernamePasswordAuthenticationFilter.class);
        /* 로그아웃 필터*/
        http
                .addFilterBefore(new CustomLogoutFilter(authService, objectMapper), LogoutFilter.class);
        /* 에러 핸들러 */
        http
         .exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
        );
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
