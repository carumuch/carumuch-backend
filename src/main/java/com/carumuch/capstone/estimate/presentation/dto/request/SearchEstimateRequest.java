package com.carumuch.capstone.estimate.presentation.dto.request;

public record SearchEstimateRequest(
	Integer minRepairCost,
	Integer maxRepairCost,
	String sido,
	String sigungu,
	Boolean isPickupRequired,
	String brand,
	Integer modelYear,
	String modelName
) {
}
