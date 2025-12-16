package com.carumuch.capstone.damage.presentation.dto.response.report;

import java.time.LocalDateTime;

public record DamageReportInfoResponse(
	Long id,
	String preferredRepairSido,
	String preferredRepairSigungu,
	Boolean isPickupRequired,
	String status,
	LocalDateTime createdAt
) {
}
