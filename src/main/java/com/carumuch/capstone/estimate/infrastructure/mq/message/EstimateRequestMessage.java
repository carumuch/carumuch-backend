package com.carumuch.capstone.estimate.infrastructure.mq.message;

public record EstimateRequestMessage(
	Long damageReportId,
	String brand,
	String imagePath
) {
}
