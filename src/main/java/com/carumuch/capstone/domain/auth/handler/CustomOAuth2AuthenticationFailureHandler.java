package com.carumuch.capstone.domain.auth.handler;

import com.carumuch.capstone.global.constants.CorsConstant;
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

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String errorMessage = URLEncoder.encode("social_login_duplicate", StandardCharsets.UTF_8);
        response.sendRedirect(CorsConstant.ALLOWED_ORIGINS + "?error=" + errorMessage);
    }
}
