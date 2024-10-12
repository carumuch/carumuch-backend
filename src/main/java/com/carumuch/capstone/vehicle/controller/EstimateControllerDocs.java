package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.vehicle.dto.estimate.*;
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
    @Operation(summary = "견적 등록 요청", description = "**성공 응답 데이터:** 견적의 `estimate_id`," +
            " 최초 견적 저장의 견적 공개 범위는 `공개` 입니다. 공개 입찰을 받지 않고 싶다면 공개 입찰 범위 수정 요청이 필요합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "견적 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> register(Long id, EstimateRegistrationReqDto estimateRegistrationReqDto);

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

    /* Select: 유저 견적 이용 내역 리스트 조회 */
    @Operation(summary = "유저 견적 이용 내역 조회", description = "**성공 응답 데이터:** EstimatePageList ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 견적 이용 내역 리스트 조회 완료"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> getEstimateHistory(int page);

    /* Update: 견적 공개 범위 수정 변경 */
    @Operation(summary = "견적 공개 범위 수정", description = "**성공 응답 데이터:** `estimate_id`, PRIVATE(비공개), PUBLIC(전체 공개) 입니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "견적 공개 범위 변경 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> updateEstimateStatus(Long id, EstimateStatusUpdateReqDto estimateStatusUpdateReqDto);

    /* Select: 견적 정보 상세 조회 */
    @Operation(summary = "사용자 개인 견적 상세 조회", description = "**성공 응답 데이터:** `estimateDto`")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "견적 상세 조회 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> detail(Long id);

    /*  Update: AI 비용 산정 반영 요청 */
    @Operation(summary = "AI 분석 금액 반영 요청", description = "**성공 응답 데이터:** `estimate_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "AI 분석 금액 반영 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> updateAIRepairCost(Long id, EstimateAIRepairCostReqDto estimateAIRepairCostReqDto);

    /* Create: 차량 Ai 견적 생성 */
//    @Operation(summary = "AI 견적 등록 요청", description = "**성공 응답 데이터:** AI 견적의 `estimate_id`," +
//            " 최초 견적 저장의 견적 공개 범위는 `비공개` 입니다. 공개 입찰을 받아보고 싶다면 공개 입찰 범위 수정 요청이 필요합니다. ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "AI 견적 등록 완료"),
//            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
//            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
//            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
//    })
//    ResponseEntity<?> registerAiEstimate(Long id, EstimateRegistrationReqDto estimateRegistrationReqDto);

    /* 차량 Ai 분석 */
//    @Operation(summary = "AI 분석 요청", description = "**성공 응답 데이터:** AI 분석 정보 ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "AI 분석 완료"),
//            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
//            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
//            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
//    })
//    ResponseEntity<?> analysis(Long id, EstimateAnalysisReqDto estimateAnalysisReqDto);
}
