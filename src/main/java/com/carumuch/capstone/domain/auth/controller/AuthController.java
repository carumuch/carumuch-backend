package com.carumuch.capstone.domain.auth.controller;

import com.carumuch.capstone.domain.auth.dto.*;
import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.carumuch.capstone.domain.auth.service.AuthService;
import com.carumuch.capstone.global.dto.ResponseDto;
import com.carumuch.capstone.global.utils.CookieUtil;
import com.carumuch.capstone.global.validation.ValidationSequence;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Date;

import static com.carumuch.capstone.global.constants.TokenConstant.*;
import static com.carumuch.capstone.global.utils.CookieUtil.createCookie;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        String accessToken = tokenProvider.resolveAccessToken(request);
        Claims claimsByAccessToken = tokenProvider.getClaimsByAccessToken(accessToken);

        String role = claimsByAccessToken.get(AUTHORITIES_KEY).toString();
        String subject = claimsByAccessToken.getSubject();
        String requestRefreshToken = CookieUtil.findCookieByName(request, REFRESH_TOKEN_COOKIE_NAME).getValue();

        boolean isValid = tokenProvider.validateRefreshTokenWithAccessTokenInfo(role, subject, requestRefreshToken);
        if (!isValid) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, createCookie(REFRESH_TOKEN_COOKIE_NAME, null, REFRESH_EXPIRATION_DELETE).toString())
                    .body(null);
        }

        String newAccessToken = tokenProvider.createAccessToken(subject, role, new Date());
        return ResponseEntity.status(OK)
                .header(AUTHORIZATION, newAccessToken)
                .body(null);
    }

    /**
     * 1. 비밀번호 찾기: 인증 번호 전송
     */
    @PostMapping("/password/reset/verification/login-id")
    public ResponseEntity<?> verificationLoginId(@Validated(ValidationSequence.class) @RequestBody VerificationLoginIdDto verificationLoginIdDto) {
        authService.sendVerificationCode(verificationLoginIdDto);
        return ResponseEntity.status(CREATED).body(ResponseDto.success(OK, null));
    }

    /**
     * 2. 비밀번호 찾기: 인증 번호 인증
     */
    @PostMapping("/password/reset/verification/code")
    public ResponseEntity<?> verificationCode(@Validated(ValidationSequence.class) @RequestBody VerificationCodeDto verificationCodeDto) {

        String temporaryToken = authService.verifyCode(verificationCodeDto);

        /* 유효기간 10분 임시 토큰 발급 */
        ResponseCookie responseCookie = ResponseCookie.from("temporary-token", temporaryToken)
                .maxAge(600)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ResponseDto.success(OK,null));
    }

    /**
     * 3. 비밀번호 찾기: 새 비밀번호트 업데이트
     */
    @PutMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@Validated(ValidationSequence.class) @RequestBody ResetPasswordDto resetPasswordDto,
                                           HttpServletRequest request) {
        /* 쿠키의 Refresh token 추출 */
        String requestTemporaryToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("temporary-token")) {
                requestTemporaryToken = cookie.getValue();
            }
        }

        /* Temporary Token 추출 -> 해당 유저가 업데이트 중 인지 확인 */
        String email = tokenProvider.getClaimsByTemporaryToken(requestTemporaryToken).getSubject();

        /* 비밀번호 변경 */
        String encodePassword = bCryptPasswordEncoder.encode(resetPasswordDto.getNewPassword());
        authService.resetPassword(email, encodePassword);

        /*성공 시 쿠키 초기화*/
        ResponseCookie responseCookie = ResponseCookie.from("temporary-token", null)
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(CREATED)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ResponseDto.success(OK, null));
    }

    /**
     * 아이디 찾기
     */
    @PostMapping("/find-id")
    public ResponseEntity<?> findLoginId(@Validated(ValidationSequence.class) @RequestBody FindLoginIdDto findLoginIdDto) {
        authService.findLoginId(findLoginIdDto.getEmail());
        return ResponseEntity.status(CREATED).body(ResponseDto.success(OK, null));
    }
}
