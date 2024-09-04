package com.carumuch.capstone.bodyshop.controller;

import com.carumuch.capstone.bodyshop.dto.BodyShopBidCreateReqDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopBidUpdateReqDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopUpdateReqDto;
import com.carumuch.capstone.bodyshop.service.BodyShopService;
import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.vehicle.dto.EstimateSearchReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/body-shops")
@RequiredArgsConstructor
public class BodyShopController implements BodyShopControllerDocs{
    private final BodyShopService bodyShopService;

    /**
     * CREATE: 신규 공업사 등록
     */
    @PostMapping
    public ResponseEntity<?> register(@Validated(ValidationSequence.class) @RequestBody BodyShopRegistrationReqDto bodyShopRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.register(bodyShopRegistrationReqDto)));
    }

    /**
     * SELECT: 공업사 키워드 검색
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchKeyword(@RequestParam(defaultValue = "1") int page, @RequestParam String keyword) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.searchKeyword(page,keyword)));
    }

    /**
     * Create: 기존 공업사로 등록
     */
    @PostMapping("/{bodyShopId}/join")
    public ResponseEntity<?> join(@PathVariable Long bodyShopId) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.join(bodyShopId)));
    }

    /**
     * UPDATE: 공업사 정보 수정
     */
    @PutMapping("/{bodyShopId}")
    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody BodyShopUpdateReqDto bodyShopUpdateReqDto,
                                    @PathVariable Long bodyShopId) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.update(bodyShopId,bodyShopUpdateReqDto)));
    }

    /**
     * UPDATE: 다른 공업사로 변경
     */
    @PatchMapping("/{bodyShopId}/transfer")
    public ResponseEntity<?> transfer(@PathVariable Long bodyShopId) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.transfer(bodyShopId)));
    }

    /**
     * SELECT: 공업사 상세 조회
     */
    @GetMapping("/{bodyShopId}")
    public ResponseEntity<?> detail(@PathVariable Long bodyShopId) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.findOne(bodyShopId)));
    }

    /**
     * SELECT: 공업사 측 견적 상세 조회
     */
    @GetMapping("/estimates/{estimateId}")
    public ResponseEntity<?> estimateDetail(@PathVariable Long estimateId) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.estimateDetail(estimateId)));
    }

    /**
     * SELECT: 공업사 측 견적 목록 상세 검색
     */
    @GetMapping("/estimates")
    public ResponseEntity<?> estimateSearch(EstimateSearchReqDto estimateSearchReqDto) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.searchEstimateList(estimateSearchReqDto)));
    }

    /**
     * CREATE: 공업사 측 특정 견적서에 대해 입찰 신청
     */
    @PostMapping("/bids/{estimateId}")
    public ResponseEntity<?> createBid(@Validated(ValidationSequence.class) @RequestBody BodyShopBidCreateReqDto bodyShopBidCreateReqDto,
                                       @PathVariable Long estimateId) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.createBid(estimateId, bodyShopBidCreateReqDto)));
    }

    /**
     * SELECT: 공업사 측 입찰 상세 조회
     */
    @GetMapping("/bids/{bidId}")
    public ResponseEntity<?> bodyShopBidDetail(@PathVariable Long bidId) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.bidDetail(bidId)));
    }

    /**
     * UPDATE: 공업사 측 입찰 정보 수정
     */
    @PutMapping("/bids/{bidId}")
    public ResponseEntity<?> updateBid(@Validated(ValidationSequence.class) @RequestBody BodyShopBidUpdateReqDto bodyShopBidUpdateReqDto,
                                       @PathVariable Long bidId) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.updateBid(bidId, bodyShopBidUpdateReqDto)));
    }

    /**
     * DELETE: 공업사 측 입찰 취소
     */
    @DeleteMapping("/bids/{bidId}")
    public ResponseEntity<?> cancelBid(@PathVariable Long bidId) {
        bodyShopService.cancelBid(bidId);
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, true));
    }

    /**
     * SELECT: 공업사 측 입찰 리스트 조회
     */
    @GetMapping("/{bodyShopId}/history/bids")
    public ResponseEntity<?> bidList(@RequestParam(defaultValue = "1") int page,
                                     @PathVariable Long bodyShopId) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.bidList(page, bodyShopId)));
    }
}
