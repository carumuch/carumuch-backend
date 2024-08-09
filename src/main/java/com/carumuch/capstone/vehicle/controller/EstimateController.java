package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.vehicle.dto.EstimateRegistrationReqDto;
import com.carumuch.capstone.vehicle.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;

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
    @PostMapping(value = "/AI/register/vehicle/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerAiEstimate(@PathVariable Long id,
                                                @RequestPart("image") MultipartFile image,
                                                @Validated(ValidationSequence.class) @RequestPart EstimateRegistrationReqDto estimateRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, estimateService.registerAIEstimate(id, image, estimateRegistrationReqDto)));
    }

    /**
     * AI 분석 요청
     */
    @PostMapping(value = "/AI/analysis/vehicle/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> Analysis(@PathVariable Long id,
                                      @RequestBody MultipartFile image) {
        estimateService.analysis(id, image);
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, null));
    }
}
