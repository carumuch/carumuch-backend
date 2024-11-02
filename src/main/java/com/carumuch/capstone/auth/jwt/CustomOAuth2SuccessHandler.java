package com.carumuch.capstone.auth.jwt;

import com.carumuch.capstone.auth.dto.CustomOAuth2User;
import com.carumuch.capstone.auth.dto.TokenDto;
import com.carumuch.capstone.auth.service.AuthService;
import com.carumuch.capstone.global.common.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String ALLOWED_ORIGINS;
    private final AuthService authService;

    public CustomOAuth2SuccessHandler(AuthService authService,
                                      @Value("${cors.allow.origins}") String ALLOWED_ORIGINS) {
        this.authService = authService;
        this.ALLOWED_ORIGINS = ALLOWED_ORIGINS;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String loginId = customUserDetails.getLoginId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        TokenDto tokenDto = authService.generateToken("Server", loginId, role);

        log.info(loginId + " : " + "login" + "(" + new Date() + ")");
        /* 응답 설정 */
        response.addHeader("Set-Cookie", createCookie("refresh-token", tokenDto.getRefreshToken()));
        response.addHeader("Set-Cookie", createCookie("authorization", tokenDto.getAccessToken()));
        response.sendRedirect(ALLOWED_ORIGINS + "/main");
    }

    private String createCookie(String key, String value) {
        return key + "=" + value + "; Max-Age=7776000; Secure; Path=/; HttpOnly; SameSite=None";
    }
}
