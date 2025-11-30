package com.carumuch.capstone.domain.bodyshop.controller;

import com.carumuch.capstone.domain.bodyshop.dto.BodyShopBidCreateReqDto;
import com.carumuch.capstone.domain.bodyshop.dto.BodyShopBidUpdateReqDto;
import com.carumuch.capstone.domain.bodyshop.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.domain.bodyshop.dto.BodyShopUpdateReqDto;
import com.carumuch.capstone.domain.estimate.dto.EstimateSearchReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;


@Validated
public interface BodyShopControllerDocs {
    /* Create: 신규 공업사 등록 */
    @Tag(name = "BodyShop")
    @Operation(summary = "신규 공업사 등록 요청", description = "**성공 응답 데이터:** 공업사의 `bodyShop_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신규 공업사 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> register(BodyShopRegistrationReqDto bodyShopRegistrationReqDto);

    /* Select: 공업사 키워드 검색 */
    @Tag(name = "BodyShop")
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
    @Tag(name = "BodyShop")
    @Operation(summary = "기존 공업사로 등록 요청", description = "**성공 응답 데이터:** 유저 `id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "기존 공업사로 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> join(Long id);

    /* Update: 공업사 정보 업데이트 */
    @Tag(name = "BodyShop")
    @Operation(summary = "공업사 정보 수정 요청", description = "**성공 응답 데이터:** 공업사 `id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공업사로 수정 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> update(BodyShopUpdateReqDto bodyShopUpdateReqDto, Long id);

    /* Update: 다른 공업사로 변경 */
    @Tag(name = "BodyShop")
    @Operation(summary = "다른 공업사로 변경 요청", description = "**성공 응답 데이터:** 유저 `id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "다른 공업사로 변경 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> transfer(Long id);

    /* Select: 공업사 상세 조회 */
    @Tag(name = "BodyShop")
    @Operation(summary = "공업사 상세 조회 요청", description = "**성공 응답 데이터:** `bodyShop` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 상세 정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> detail(Long id);

    /* Select: 공업사 측 견적 상세 조회 */
    @Tag(name = "BodyShop")
    @Operation(summary = "공업사 측 견적 상세 조회 요청", description = "**성공 응답 데이터:** `estimate` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 측 견적 상세 정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> estimateDetail(Long id);

    /* Select: 공업사 측 견적 목록 상세 검색 요청 */
    @Tag(name = "BodyShop")
    @Operation(summary = "공업사 측 견적 목록 상세 검색 요청, 동적 쿼리 파라미터 요청, 요청필드 aiEstimatedRepairCost 값 이하인 견적서만 조회", description = "**성공 응답 데이터:** `estimatePage` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 측 견적 목록 상세 검색 요청 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> estimateSearch(EstimateSearchReqDto estimateSearchReqDto);


    /* Create: 공업사 입찰 신청 */
    @Tag(name = "Bid-BodyShop")
    @Operation(summary = "공업사 측 입찰 생성(신청) 입니다.", description = "**성공 응답 데이터:** `Bid_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공업사 입찰 신청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "401", description = "이미 신청 이력이 있습니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> createBid(BodyShopBidCreateReqDto bodyShopBidCreateReqDto, Long estimateId);

    /* Select: 공업사 입찰 상세 */
    @Tag(name = "Bid-BodyShop")
    @Operation(summary = "공업사 측 입찰 상세 조회 입니다.", description = "**성공 응답 데이터:** `Bid` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공업사 입찰 조회 성공"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> bodyShopBidDetail(Long id);

    /* Update: 공업사 입찰 수정 */
    @Tag(name = "Bid-BodyShop")
    @Operation(summary = "공업사 측 입찰 정보 수정 입니다.", description = "**성공 응답 데이터:** `Bid_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공업사 입찰 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> updateBid(BodyShopBidUpdateReqDto bodyShopBidUpdateReqDto, Long bidId);

    /* Delete: 공업사 입찰 삭제 */
    @Tag(name = "Bid-BodyShop")
    @Operation(summary = "공업사 측 입찰 취소 입니다.", description = "**성공 응답 데이터:** `true` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공업사 입찰 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 공업사 입니다."),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> cancelBid(Long bidId);

    /* Select: 공업사 입찰 신청 리스트 */
    @Tag(name = "Bid-BodyShop")
    @Operation(summary = "공업사 측 입찰 신청 리스트 입니다.", description = "**성공 응답 데이터:** `BidPage` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공업사 입찰 리스트 조회 성공"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> bidList(int page, Long bodyShopId);
}
