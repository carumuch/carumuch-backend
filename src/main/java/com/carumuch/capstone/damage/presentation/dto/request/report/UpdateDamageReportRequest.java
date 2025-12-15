package com.carumuch.capstone.damage.presentation.dto.request.report;

import jakarta.validation.constraints.NotNull;

public record UpdateDamageReportRequest(
	@NotNull String description,
	@NotNull String preferredRepairSido,
	@NotNull String preferredRepairSigungu,
	@NotNull Boolean isPickupRequired
) {
}
