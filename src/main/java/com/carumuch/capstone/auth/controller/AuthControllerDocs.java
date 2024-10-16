package com.carumuch.capstone.auth.controller;

import com.carumuch.capstone.auth.dto.LoginReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
public interface AuthControllerDocs {

    /* 로그인 -> Filter 로 진행 */
    @Operation(summary = "로그인 요청", description = "**성공 응답 데이터:** 헤더의 `토큰` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패"),
    })
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginReqDto loginReqDto);

    /* 로그아웃 -> Filter 로 진행 */
    @Operation(summary = "로그아웃 요청", description = "**성공 응답 데이터:** 헤더의 `쿠키 0` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "로그아웃 실패"),
    })
    @PostMapping("/logout")
    ResponseEntity<?> logout();

    /* 재발급 */
    @Operation(summary = "재발급 요청", description = "**성공 데이터:** 헤더의 `토큰`," +
            " 유효성 검증이 안된 토큰이라면 `Refresh Token 초기화 진행 후 재로그인`을 유도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "재발급 실패, 유효한 토큰이 아닙니다, 제 로그인이 필요합니다."),
    })
    ResponseEntity<?> reissue(HttpServletRequest request);
}
