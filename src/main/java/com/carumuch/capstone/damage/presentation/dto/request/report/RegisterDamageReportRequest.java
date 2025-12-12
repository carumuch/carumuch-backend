package com.carumuch.capstone.damage.presentation.dto.request.report;

import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.RepairRegion;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;

import jakarta.validation.constraints.NotNull;

public record RegisterDamageReportRequest(
	@NotNull String description,
	@NotNull String preferredRepairSido,
	@NotNull String preferredRepairSigungu,
	@NotNull Boolean isisPickupRequired,
	@NotNull String imagePath
) {
	public DamageReport toEntity(Vehicle vehicle) {
		return new DamageReport(
			description,
			new RepairRegion(preferredRepairSido, preferredRepairSigungu),
			isisPickupRequired,
			imagePath,
			vehicle
		);
	}
}
