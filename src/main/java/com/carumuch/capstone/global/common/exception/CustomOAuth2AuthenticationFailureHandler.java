package com.carumuch.capstone.global.common.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final String ALLOWED_ORIGINS;
    public CustomOAuth2AuthenticationFailureHandler(@Value("${cors.allow.origins}") String ALLOWED_ORIGINS) {
        this.ALLOWED_ORIGINS = ALLOWED_ORIGINS;
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String errorMessage = URLEncoder.encode("social_login_duplicate", StandardCharsets.UTF_8);
        response.sendRedirect(ALLOWED_ORIGINS + "?error=" + errorMessage);
    }
}
