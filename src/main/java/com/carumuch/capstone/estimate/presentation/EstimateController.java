package com.carumuch.capstone.estimate.presentation;

import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.estimate.application.EstimateService;
import com.carumuch.capstone.estimate.presentation.dto.request.UpdateEstimateStatusRequest;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateController {
    private final EstimateService estimateService;

	@GetMapping("/{estimateId}")
	public ResponseEntity<ApiResponse<EstimateDetailResponse>> findEstimateDetail(@PathVariable Long estimateId) {
		return ResponseEntity.ok().body(ApiResponse.of(estimateService.findEstimateDetail(estimateId)));
	}

	@PutMapping( "/{estimateId}/status")
	public ResponseEntity<ApiResponse<Void>> changeStatus(@PathVariable Long estimateId, @RequestBody UpdateEstimateStatusRequest updateEstimateStatusRequest) {
		estimateService.changeStatus(estimateId, updateEstimateStatusRequest.status());
		return ResponseEntity.ok().body(ApiResponse.of());
	}
}
