package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.vehicle.dto.EstimateRegistrationReqDto;
import com.carumuch.capstone.vehicle.dto.EstimateUpdateReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;


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
    ResponseEntity<?> register(Long id, MultipartFile image, EstimateRegistrationReqDto estimateRegistrationReqDto);

    /* Create: 차량 Ai 견적 생성 */
    @Operation(summary = "AI 견적 등록 요청", description = "**성공 응답 데이터:** AI 견적의 `estimate_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "AI 견적 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> registerAiEstimate(Long id, MultipartFile file, EstimateRegistrationReqDto estimateRegistrationReqDto);

    /* 차량 Ai 분석 */
    @Operation(summary = "AI 분석 요청", description = "**성공 응답 데이터:** AI 분석 정보 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "AI 분석 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> analysis(Long id, MultipartFile file);

    /*  Update: 견적 수정 */
    @Operation(summary = "견적 수정 요청", description = "**성공 응답 데이터:** `estimate_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "견적 수정 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> update(Long id, EstimateUpdateReqDto estimateUpdateReqDto);

    /*  Delete: 견적 삭제 */
    @Operation(summary = "견적 삭제 요청", description = "**성공 응답 데이터:** true ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "견적 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> delete(Long id);

    /* Select: 견적 히스토리 리스트 차량별 조회*/
    @Operation(summary = "견적 히스토리 리스트 차량별 조회", description = "**성공 응답 데이터:** EstimatePageList ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "차량 별 견적 리스트 조회 완료"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> getEstimateHistoryByVehicleId(int page, Long id);

}
