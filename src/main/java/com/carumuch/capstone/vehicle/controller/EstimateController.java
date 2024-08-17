package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.vehicle.dto.EstimateRegistrationReqDto;
import com.carumuch.capstone.vehicle.dto.EstimateStatusUpdateReqDto;
import com.carumuch.capstone.vehicle.dto.EstimateUpdateReqDto;
import com.carumuch.capstone.vehicle.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateController implements EstimateControllerDocs{
    private final EstimateService estimateService;

    /**
     * CREATE: 차량 견적 등록
     */
    @PostMapping(value = "/register/vehicle/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@PathVariable Long id,
                                      @RequestPart("image") MultipartFile image,
                                      @Validated(ValidationSequence.class) @RequestPart EstimateRegistrationReqDto estimateRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.register(id, image, estimateRegistrationReqDto)));
    }

    /**
     * CREATE: 차량 AI 견적 등록
     */
    @PostMapping(value = "/ai/register/vehicle/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerAiEstimate(@PathVariable Long id,
                                                @RequestPart("image") MultipartFile image,
                                                @Validated(ValidationSequence.class) @RequestPart EstimateRegistrationReqDto estimateRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.registerAIEstimate(id, image, estimateRegistrationReqDto)));
    }

    /**
     * AI 분석 요청
     */
    @PostMapping(value = "/ai/analysis/vehicle/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analysis(@PathVariable Long id,
                                      @RequestBody MultipartFile image) {
        estimateService.analysis(id, image);
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, null));
    }

    /**
     * UPDATE: 견적 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Validated(ValidationSequence.class) @RequestBody EstimateUpdateReqDto estimateUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.update(id, estimateUpdateReqDto)));
    }

    /**
     * DELETE: 견적 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        estimateService.delete(id);
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, null));
    }

    /**
     * SELECT: 견적 히스토리 리스트 차량별 조회
     */
    @GetMapping("/history/vehicle/{id}")
    public ResponseEntity<?> getEstimateHistoryByVehicleId(@RequestParam(defaultValue = "1") int page, @PathVariable Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, estimateService.getEstimateHistoryByVehicleId(page, id)));
    }

    /**
     * SELECT: 유저 견적 이용 내역 조회
     */
    @GetMapping("/history")
    public ResponseEntity<?> getEstimateHistory(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, estimateService.getEstimateHistory(page)));
    }

    /**
     * UPDATE: 견적 공개 범위 수정
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateEstimateStatus(@PathVariable Long id,
                                                  @Validated(ValidationSequence.class) @RequestBody EstimateStatusUpdateReqDto estimateStatusUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.updateEstimateStatus(id, estimateStatusUpdateReqDto)));
    }

    /**
     * SELECT: 사용자 개인 견적 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, estimateService.detail(id)));
    }
}