package com.carumuch.capstone.domain.auth.filter;

import com.carumuch.capstone.domain.auth.dto.LoginReqDto;
import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;

@Slf4j
public class CustomUserLoginFilter extends CustomUsernamePasswordAuthenticationFilter {

    public CustomUserLoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ObjectMapper objectMapper) {
        super(authenticationManager, tokenProvider, objectMapper);
        this.setFilterProcessesUrl("/login");
    }

    @Override
    protected void validateLoginRequestDto(LoginReqDto loginReqDto) {
        if (loginReqDto.getLoginId() == null || !loginReqDto.getLoginId().matches("^[a-z0-9]{4,20}$")) {
            log.error("401error -> 올바르지 않은 학생 로그인 아이디 요청");
            throw new IllegalArgumentException("올바르지 않은 로그인 아이디 요청");
        }
        if (loginReqDto.getPassword() == null || !loginReqDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$")) {
            log.error("401error -> 올바르지 않은 학생 로그인 비밀번호 요청");
            throw new IllegalArgumentException("올바르지 않은 로그인 비밀번호 요청");
        }
    }
}
