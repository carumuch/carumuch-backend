package com.carumuch.capstone.estimate.presentation;

import com.carumuch.capstone.estimate.application.EstimateService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateController {
    private final EstimateService estimateService;

}
