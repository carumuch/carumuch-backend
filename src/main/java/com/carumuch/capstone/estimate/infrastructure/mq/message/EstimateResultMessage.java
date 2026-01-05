package com.carumuch.capstone.estimate.infrastructure.mq.message;

import java.util.Set;

public record EstimateResultMessage(
	Long damageReportId,
	int repairCost,
	Set<String> repairParts,
	String imagePath
) {
}
