package com.carumuch.capstone.auth.jwt;

import com.carumuch.capstone.auth.service.AuthService;
import com.carumuch.capstone.global.common.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public CustomLogoutFilter(AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        /* 경로, Http 메소드 */
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        /* Refresh Token 추출 */
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh-token")) {
                refresh = cookie.getValue();
            }
        }
        String accessToken = request.getHeader("Authorization");

        authService.logout(accessToken);

        /* 응답 설정 */
        Cookie cookie = new Cookie("refresh-token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(
                ResponseDto.success(HttpStatus.OK,null)));
    }
}
