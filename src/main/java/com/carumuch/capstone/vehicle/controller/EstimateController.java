package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.vehicle.dto.EstimateRegistrationReqDto;
import com.carumuch.capstone.vehicle.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateController implements EstimateControllerDocs{
    private final EstimateService estimateService;

    /**
     * CREATE: 차량 견적 등록
     */
    @PostMapping("/register/vehicle/{id}")
    public ResponseEntity<?> register(@PathVariable Long id,
                                      @Validated(ValidationSequence.class) @RequestBody EstimateRegistrationReqDto estimateRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.register(id,estimateRegistrationReqDto)));
    }

    /**
     * 차량 AI 견적 등록
     */
    @PostMapping("/AI/register/vehicle/{id}")
    public ResponseEntity<?> registerAiEstimate(@PathVariable Long id,
                                                @Validated(ValidationSequence.class) @RequestBody EstimateRegistrationReqDto estimateRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.registerAIEstimate(id,estimateRegistrationReqDto)));
    }
}
