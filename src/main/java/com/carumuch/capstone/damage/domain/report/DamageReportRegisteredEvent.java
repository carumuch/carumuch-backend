package com.carumuch.capstone.damage.domain.report;

public record DamageReportRegisteredEvent(
	Long damageReportId,
	String imagePath,
	String brand
) {
}
