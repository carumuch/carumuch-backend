package com.carumuch.capstone.damage.presentation.dto.response;

import com.carumuch.capstone.damage.domain.Vehicle;

public record VehicleInfoResponse(
	Long id,
	String licenseNumber,
	String ownershipType,
	String brand,
	int modelYear,
	String modelName,
	String ownerName
) {
	public static VehicleInfoResponse from(Vehicle vehicle) {
		return new VehicleInfoResponse(
			vehicle.getId(),
			vehicle.getLicenseNumber().getValue(),
			vehicle.getOwnershipType().name(),
			vehicle.getBrand(),
			vehicle.getModelYear(),
			vehicle.getModelName(),
			vehicle.getOwnerName()
		);
	}
}
