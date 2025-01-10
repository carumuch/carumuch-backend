package com.carumuch.capstone.domain.auth.controller;

import com.carumuch.capstone.domain.auth.dto.*;
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

    @Operation(summary = "1. 비밀번호 찾기 -> 인증 번호 발급", description = "**성공 데이터:** true," +
            " 존재하는 아이디라면 해당 아이디의 이메일로 인증번호가 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일로 인증번호 전송 성공"),
            @ApiResponse(responseCode = "400", description = "찾을 수 없는 아이디 입니다."),
    })
    ResponseEntity<?> verificationLoginId(VerificationLoginIdDto VerificationLoginIdDto);

    @Operation(summary = "2. 비밀번호 찾기 -> 인증 번호 인증", description = "**성공 데이터:** 임시 토큰 쿠키 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 인증 성공"),
            @ApiResponse(responseCode = "400", description = "인증번호가 잘 못 되었습니다."),
    })
    ResponseEntity<?> verificationCode(VerificationCodeDto verificationCodeDto);

    @Operation(summary = "3. 비밀번호 찾기 -> 새 비밀번호 업데이트", description = "**성공 데이터:** true , 임시 토큰 쿠키 초기화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "새 비밀번호 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효시간 초과, 다시 시도")
    })
    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto, HttpServletRequest request);

    @Operation(summary = "아이디 찾기", description = "**성공 데이터:** true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
    })
    ResponseEntity<?> findLoginId(FindLoginIdDto findLoginIdDto);
}
