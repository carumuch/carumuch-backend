package com.carumuch.capstone.domain.auth.filter;

import com.carumuch.capstone.domain.auth.dto.LoginReqDto;
import com.carumuch.capstone.domain.auth.dto.TokenDto;
import com.carumuch.capstone.domain.auth.service.AuthService;
import com.carumuch.capstone.global.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
import java.util.Iterator;

import static org.springframework.http.HttpStatus.*;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, AuthService authService, ObjectMapper objectMapper) {

        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginReqDto loginReqDto = new LoginReqDto();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginReqDto = objectMapper.readValue(messageBody, LoginReqDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* 아이디 검증 */
        if (loginReqDto.getLoginId() == null ||
                !loginReqDto.getLoginId().matches("^[a-z0-9]{4,20}$")) {
            log.error("401error -> 잘못된 아이디 값");
            throw new InternalAuthenticationServiceException(loginReqDto.getLoginId());
        }

        /* 비밀번호 검증 */
        if (loginReqDto.getPassword() == null ||
                !loginReqDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$")) {
            log.error("401error -> 잘못된 비밀번호 값");
            throw new BadCredentialsException(loginReqDto.getPassword());
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginReqDto.getLoginId(), loginReqDto.getPassword(), null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        /* 사용자 정보 */
        String loginId = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        TokenDto tokenDto = authService.generateToken("Server", loginId, role);

        log.info(loginId + " : " + "login" + "(" + new Date() + ")");

        /* 응답 설정 */
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Set-Cookie", createCookie("refresh-token", tokenDto.getRefreshToken()));

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(
                ResponseDto.success(OK,null)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        /* 실패 응답 설정 */
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(
                ResponseDto.fail(UNAUTHORIZED,"아이디 혹은 비밀번호가 일치하지 않습니다.")));
    }

    private String createCookie(String key, String value) {
        return key + "=" + value + "; Max-Age=7776000; Secure; Path=/; HttpOnly; SameSite=None";
    }
}

