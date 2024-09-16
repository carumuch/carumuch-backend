package com.carumuch.capstone.bodyshop.controller;

import com.carumuch.capstone.bodyshop.dto.bid.BidStatusUpdateReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Bid")
@Validated
public interface BidControllerDocs {

    /* Select: 사용자 특정 견석서의 입찰 내역 리스트 조회 */
    @Operation(summary = "사용자 특정 견석서의 입찰 내역 리스트 조회", description = "**성공 응답 데이터:** 입찰 내역 `PageList` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입찰 리스트 조회 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> bidPage(@Min(value = 1, message = "페이지는 0 이상이어야 합니다.") int page, Long id);

    /* Select: 사용자 입찰 상세 조회 조회 */
    @Operation(summary = "사용자 입찰 상세 조회", description = "**성공 응답 데이터:** `bid` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입찰 상세 조회 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> bidDetail(Long id);

    /* Create: 사용자 입찰 상태 변경 */
    @Operation(summary = "사용자 입찰 상태 변경", description = "**성공 응답 데이터:**  `bid_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "입찰 상태 변경 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "409", description = "이미 체결된 이력이 존재합니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> bidStatusUpdate(Long id, BidStatusUpdateReqDto bidStatusUpdateReqDto);

}
