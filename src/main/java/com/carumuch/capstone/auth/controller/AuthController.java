package com.carumuch.capstone.auth.controller;

import com.carumuch.capstone.auth.dto.LoginReqDto;
import com.carumuch.capstone.auth.dto.TokenDto;
import com.carumuch.capstone.auth.service.AuthService;
import com.carumuch.capstone.global.common.ResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
}
