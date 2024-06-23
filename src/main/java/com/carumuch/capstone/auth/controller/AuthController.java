package com.carumuch.capstone.auth.controller;

import com.carumuch.capstone.auth.dto.*;
import com.carumuch.capstone.auth.service.AuthService;
import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final long COOKIE_EXPIRATION = 7776000; // 90일

    /* 로그인 Docs */
    @Override
    public ResponseEntity<?> login(LoginReqDto loginReqDto) {
        return null;
    }

    /* 로그아웃 Docs */
    @Override
    public ResponseEntity<?> logout() {
        return null;
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        /* 쿠키의 Refresh token 추출 */
        String requestRefreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh-token")) {
                requestRefreshToken = cookie.getValue();
            }
        }
        /* Access Token 추출 -> Refresh token 키 값에 사용 */
        String requestAccessToken = request.getHeader("Authorization");

        TokenDto reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        if (reissuedTokenDto != null) {
            /* 토큰 재발급 성공*/
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            return ResponseEntity
                    .status(OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .body(ResponseDto.success(OK,null));
        } else {
            /* Refresh Token 탈취 가능성 */
            /* Cookie 초기화 후 재로그인 유도 */
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", null)
                    .maxAge(0)
                    .path("/")
                    .build();
            return ResponseEntity
                    .status(UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(ResponseDto.fail(UNAUTHORIZED,"토큰이 재발급 실패, 다시 로그인 해야합니다."));
        }
    }

    /**
     * 1. 비밀번호 찾기: 인증 번호 전송
     */
    @PostMapping("/password/verification/login-id")
    public ResponseEntity<?> verificationLoginId(@Validated(ValidationSequence.class) @RequestBody VerificationLoginIdDto VerificationLoginIdDto) {
        return null; // true
    }

    /**
     * 2. 비밀번호 찾기: 인증 번호 인증
     */
    @PostMapping("/password/verification/code")
    public ResponseEntity<?> verificationCode(@Validated(ValidationSequence.class) @RequestBody VerificationCodeDto verificationCodeDto) {
        return null; // jwt 토큰
    }

    /**
     * 3. 비밀번호 찾기: 새 비밀번호트 업데이트
     */
    @PutMapping("/auth/password/reset")
    public ResponseEntity<?> resetPassword(@Validated(ValidationSequence.class) @RequestBody ResetPasswordDto resetPasswordDto,
                                           HttpServletRequest request) {
        /* Access Token 추출 -> 해당 유저가 업데이트 중 인지 확인 */
        String requestAccessToken = request.getHeader("Authorization");
        String email = authService.getPrincipal(requestAccessToken);

        /* 비밀번호 변경 */
        authService.resetPassword(email, resetPasswordDto);

        return ResponseEntity.status(CREATED).body(ResponseDto.success(OK, null));
    }
}
