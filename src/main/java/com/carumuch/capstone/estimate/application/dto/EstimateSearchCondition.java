package com.carumuch.capstone.estimate.application.dto;

import com.carumuch.capstone.estimate.presentation.dto.request.SearchEstimateRequest;

public record EstimateSearchCondition(
	Integer minRepairCost,
	Integer maxRepairCost,
	String sido,
	String sigungu,
	Boolean isPickupRequired,
	String brand,
	Integer modelYear,
	String modelName
) {
	public static EstimateSearchCondition from(SearchEstimateRequest request) {
		return new EstimateSearchCondition(
			request.minRepairCost(),
			request.maxRepairCost(),
			request.sido(),
			request.sigungu(),
			request.isPickupRequired(),
			request.brand(),
			request.modelYear(),
			request.modelName()
		);
	}
}
