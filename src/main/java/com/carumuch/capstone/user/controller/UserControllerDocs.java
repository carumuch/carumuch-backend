package com.carumuch.capstone.user.controller;

import com.carumuch.capstone.user.dto.JoinReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Users")
public interface UserControllerDocs {

    @Operation(summary = "회원가입 요청", description = "**성공 응답 데이터:** 사용자의 `user_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "409", description = "아이디 중복")
    })
    ResponseEntity<?> join(JoinReqDto joinReqDto);
}
