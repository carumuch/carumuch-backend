package com.carumuch.capstone.estimate.presentation;

import com.carumuch.capstone.common.legacy.dto.ResponseDto;
import com.carumuch.capstone.estimate.application.EstimateService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateController {
    private final EstimateService estimateService;

    @GetMapping("/{estimateId}")
    public ResponseEntity<?> detail(@PathVariable("estimateId") Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, estimateService.detail(id)));
    }
}
