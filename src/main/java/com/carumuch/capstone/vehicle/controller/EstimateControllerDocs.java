package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.vehicle.dto.EstimateRegistrationReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;


@Tag(name = "Estimate")
@Validated
public interface EstimateControllerDocs {
    /* Create: 차량 견적 생성 */
    @Operation(summary = "견적 등록 요청", description = "**성공 응답 데이터:** 견적의 `estimate_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "견적 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> register(Long id, EstimateRegistrationReqDto estimateRegistrationReqDto);

    /* Create: 차량 Ai 견적 생성 */
    @Operation(summary = "AI 견적 등록 요청", description = "**성공 응답 데이터:** AI 견적의 `estimate_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "AI 견적 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> registerAiEstimate(Long id,EstimateRegistrationReqDto estimateRegistrationReqDto);
}
