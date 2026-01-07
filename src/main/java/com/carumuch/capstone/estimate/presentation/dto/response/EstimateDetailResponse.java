package com.carumuch.capstone.estimate.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.estimate.domain.Estimate;

public record EstimateDetailResponse(
	Long estimateId,
	Integer repairCost,
	List<String> repairParts,
	String estimateStatus,
	String imagePath,
	LocalDateTime createdDate,
	VehicleInfo vehicleInfo,
	DamageReportInfo damageReportInfo
) {
	public EstimateDetailResponse(Estimate estimate) {
		this(
			estimate.getId(),
			estimate.getRepairCost(),
			estimate.getRepairParts().stream()
				.sorted()
				.toList(),
			estimate.getEstimateStatus().name(),
			estimate.getImagePath(),
			estimate.getCreateDate(),
			new VehicleInfo(estimate.getDamageReport().getVehicle()),
			new DamageReportInfo(estimate.getDamageReport())
		);
	}

	private record VehicleInfo(
		String licenseNumber,
		String ownershipType,
		String brand,
		Integer modelYear,
		String modelName,
		String ownerName
	) {
		private VehicleInfo(Vehicle vehicle) {
			this(
				vehicle.getLicenseNumber().getValue(),
				vehicle.getOwnershipType().name(),
				vehicle.getBrand(),
				vehicle.getModelYear(),
				vehicle.getModelName(),
				vehicle.getOwnerName()
			);
		}
	}

	private record DamageReportInfo(
		String preferredRepairSido,
		String preferredRepairSigungu,
		String description,
		Boolean isPickupRequired
	) {
		private DamageReportInfo(DamageReport damageReport) {
			this(
				damageReport.getPreferredRepairRegion().getSido(),
				damageReport.getPreferredRepairRegion().getSigungu(),
				damageReport.getDescription(),
				damageReport.isPickupRequired()
			);
		}
	}
}
