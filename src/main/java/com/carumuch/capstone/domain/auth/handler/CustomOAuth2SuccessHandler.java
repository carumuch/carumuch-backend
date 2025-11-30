package com.carumuch.capstone.domain.auth.handler;

import com.carumuch.capstone.domain.auth.dto.CustomOAuth2User;
import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.carumuch.capstone.global.constants.CorsConstant;
import com.carumuch.capstone.global.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import static com.carumuch.capstone.global.constants.TokenConstant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String loginId = customUserDetails.getLoginId();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String accessToken = tokenProvider.createAccessToken(loginId, role, new Date());
        String refreshToken = tokenProvider.createRefreshToken(loginId, role, new Date());

        response.addHeader(HttpHeaders.SET_COOKIE,
                CookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, accessToken, tokenProvider.getAccessTokenExpirationSeconds()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                CookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, tokenProvider.getRefreshTokenExpirationSeconds()).toString());
        response.sendRedirect(CorsConstant.ALLOWED_ORIGINS + "/main");
        log.info(loginId + " : " + "login" + "(" + new Date() + ")");
    }
}
