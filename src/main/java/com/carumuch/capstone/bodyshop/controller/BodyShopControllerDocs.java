package com.carumuch.capstone.bodyshop.controller;

import com.carumuch.capstone.bodyshop.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopUpdateReqDto;
import com.carumuch.capstone.vehicle.dto.EstimateSearchReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "BodyShop")
@Validated
public interface BodyShopControllerDocs {
    /* Create: 신규 공업사 등록 */
    @Operation(summary = "신규 공업사 등록 요청", description = "**성공 응답 데이터:** 공업사의 `bodyShop_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신규 공업사 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> register(BodyShopRegistrationReqDto bodyShopRegistrationReqDto);

    /* Select: 공업사 키워드 검색 */
    @Operation(summary = "공업사 키워드 검색 요청", description = "**성공 응답 데이터:** 공업사 `PageList` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 키워드 검색 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> searchKeyword(@Min(value = 1, message = "페이지는 0 이상이어야 합니다.") int id,
                                    @Pattern(regexp = ".{2,}", message = "검색어는 2개의 문자 이상이어야 합니다.") String keyword);

    /* Update: 기존 공업사로 등록 */
    @Operation(summary = "기존 공업사로 등록 요청", description = "**성공 응답 데이터:** 유저 `id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "기존 공업사로 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> join(Long id);

    /* Update: 공업사 정보 업데이트 */
    @Operation(summary = "공업사 정보 수정 요청", description = "**성공 응답 데이터:** 공업사 `id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공업사로 수정 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> update(BodyShopUpdateReqDto bodyShopUpdateReqDto, Long id);

    /* Update: 다른 공업사로 변경 */
    @Operation(summary = "다른 공업사로 변경 요청", description = "**성공 응답 데이터:** 유저 `id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "다른 공업사로 변경 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> transfer(Long id);

    /* Select: 공업사 상세 조회 */
    @Operation(summary = "공업사 상세 조회 요청", description = "**성공 응답 데이터:** `bodyShop` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 상세 정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "400", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> detail(Long id);

    /* Select: 공업사 측 견적 상세 조회 */
    @Operation(summary = "공업사 측 견적 상세 조회 요청", description = "**성공 응답 데이터:** `estimate` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 측 견적 상세 정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "400", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> estimateDetail(Long id);

    /* Select: 공업사 측 견적 목록 상세 검색 요청 */
    @Operation(summary = "공업사 측 견적 목록 상세 검색 요청", description = "**성공 응답 데이터:** `estimatePage` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 측 견적 목록 상세 검색 요청 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "400", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> estimateSearch(EstimateSearchReqDto estimateSearchReqDto);
}
