package com.carumuch.capstone.domain.estimate.controller;

import com.carumuch.capstone.domain.estimate.dto.EstimateAIRepairCostReqDto;
import com.carumuch.capstone.domain.estimate.dto.EstimateRegistrationReqDto;
import com.carumuch.capstone.domain.estimate.dto.EstimateStatusUpdateReqDto;
import com.carumuch.capstone.domain.estimate.dto.EstimateUpdateReqDto;
import com.carumuch.capstone.global.dto.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.domain.estimate.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateController implements EstimateControllerDocs {
    private final EstimateService estimateService;

    /**
     * CREATE: 차량 견적 등록
     */
    @PostMapping("/register/vehicle/{vehicleId}")
    public ResponseEntity<?> register(@PathVariable("vehicleId") Long id,
                                      @Validated(ValidationSequence.class) @RequestBody EstimateRegistrationReqDto estimateRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.register(id, estimateRegistrationReqDto)));
    }

    /**
     * UPDATE: 견적 수정
     */
    @PutMapping("/{estimateId}")
    public ResponseEntity<?> update(@PathVariable("estimateId") Long id,
                                    @Validated(ValidationSequence.class) @RequestBody EstimateUpdateReqDto estimateUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.update(id, estimateUpdateReqDto)));
    }

    /**
     * DELETE: 견적 삭제
     */
    @DeleteMapping("/{estimateId}")
    public ResponseEntity<?> delete(@PathVariable("estimateId") Long id) {
        estimateService.delete(id);
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, null));
    }

    /**
     * SELECT: 견적 히스토리 리스트 차량별 조회
     */
    @GetMapping("/history/vehicle/{vehicleId}")
    public ResponseEntity<?> getEstimateHistoryByVehicleId(@RequestParam(defaultValue = "1") int page, @PathVariable("vehicleId") Long id) {
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
    @PutMapping("/{estimateId}/status")
    public ResponseEntity<?> updateEstimateStatus(@PathVariable("estimateId") Long id,
                                                  @Validated(ValidationSequence.class) @RequestBody EstimateStatusUpdateReqDto estimateStatusUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.updateEstimateStatus(id, estimateStatusUpdateReqDto)));
    }

    /**
     * SELECT: 사용자 개인 견적 상세 조회
     */
    @GetMapping("/{estimateId}")
    public ResponseEntity<?> detail(@PathVariable("estimateId") Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, estimateService.detail(id)));
    }

    /**
     * UPDATE: AI 분석 결과 금액 반영 요청
     */
    @PutMapping("/{estimateId}/ai-repair-cost")
    public ResponseEntity<?> updateAIRepairCost(@PathVariable("estimateId") Long id,
                                                @RequestBody EstimateAIRepairCostReqDto estimateAIRepairCostReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.updateAICost(id, estimateAIRepairCostReqDto)));
    }

    /**
     * info: 요구사항 변경 -> AI 통신을 server to server 로 수행하지 않는 것 으로 변경되었습니다.
     * Date: 2024.10.12
     */

    /**
     * CREATE: 차량 AI 견적 등록
     */
//    @PostMapping("/ai/register/vehicle/{vehicleId}")
//    public ResponseEntity<?> registerAiEstimate(@PathVariable("vehicleId") Long id,
//                                                @Validated(ValidationSequence.class) @RequestBody EstimateRegistrationReqDto estimateRegistrationReqDto) {
//        return ResponseEntity.status(CREATED)
//                .body(ResponseDto.success(CREATED, estimateService.registerAIEstimate(id, estimateRegistrationReqDto)));
//    }

    /**
     * AI 분석 요청
     */
//    @PostMapping("/ai/analysis/vehicle/{vehicleId}")
//    public ResponseEntity<?> analysis(@PathVariable("vehicleId") Long id,
//                                      @RequestBody EstimateAnalysisReqDto estimateAnalysisReqDto) {
//        estimateService.analysis(id, estimateAnalysisReqDto.getImagePath());
//        return ResponseEntity.status(CREATED)
//                .body(ResponseDto.success(CREATED, null));
//    }
}
