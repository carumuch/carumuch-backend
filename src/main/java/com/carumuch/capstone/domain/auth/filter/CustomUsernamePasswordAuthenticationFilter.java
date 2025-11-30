package com.carumuch.capstone.domain.auth.filter;

import com.carumuch.capstone.domain.auth.dto.LoginReqDto;
import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.carumuch.capstone.global.utils.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

import static com.carumuch.capstone.global.constants.TokenConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public abstract class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginReqDto loginReqDto;
        try {
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            loginReqDto = objectMapper.readValue(messageBody, LoginReqDto.class);
            validateLoginRequestDto(loginReqDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginReqDto.getLoginId(), loginReqDto.getPassword(), null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        handleSuccessAuthentication(response, authentication);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        handleFailureAuthentication(response);
    }

    private void handleSuccessAuthentication(HttpServletResponse response, Authentication authentication) throws IOException {
        String id = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new RuntimeException("권한이 식별되지 않은 사용자 입니다. : " + id));

        log.info("{}-{}: login ({})", id, role, new Date());
        ResponseCookie refreshTokenCookie = CookieUtil.createCookie(
                REFRESH_TOKEN_COOKIE_NAME,
                tokenProvider.createRefreshToken(id, role, new Date()),
                tokenProvider.getRefreshTokenExpirationSeconds()
        );

        response.setHeader(AUTHORIZATION, BEARER_PREFIX + tokenProvider.createAccessToken(id, role, new Date()));
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(null));
    }

    private void handleFailureAuthentication(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString("아이디 혹은 비밀번호가 올바르지 않습니다."));
    }

    protected abstract void validateLoginRequestDto(LoginReqDto loginReqDto);
}