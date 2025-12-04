package com.carumuch.capstone.vehicle.presentation.dto;

import com.carumuch.capstone.vehicle.domain.Vehicle;

public record VehicleInfoResDto(
	Long id,
	String licenseNumber,
	String ownershipType,
	String brand,
	int modelYear,
	String modelName,
	String ownerName
) {
	public static VehicleInfoResDto from(Vehicle vehicle) {
		return new VehicleInfoResDto(
			vehicle.getId(),
			vehicle.getLicenseNumber(),
			vehicle.getOwnershipType().name(),
			vehicle.getBrand(),
			vehicle.getModelYear(),
			vehicle.getModelName(),
			vehicle.getOwnerName()
		);
	}
}
