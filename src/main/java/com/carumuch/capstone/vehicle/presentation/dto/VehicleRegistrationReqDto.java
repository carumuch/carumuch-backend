package com.carumuch.capstone.vehicle.presentation.dto;

import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.domain.VehicleOwnershipType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRegistrationReqDto(
	@NotBlank String licenseNumber,
	@NotBlank String ownershipType,
	@NotBlank String brand,
	@NotNull int modelYear,
	@NotBlank String modelName,
	@NotBlank String ownerName
) {
	public Vehicle toEntity(VehicleRegistrationReqDto vehicleRegistrationReqDto, User user) {
		return new Vehicle(
			vehicleRegistrationReqDto.licenseNumber,
			VehicleOwnershipType.from(vehicleRegistrationReqDto.ownershipType),
			vehicleRegistrationReqDto.brand,
			vehicleRegistrationReqDto.modelYear,
			vehicleRegistrationReqDto.modelName,
			vehicleRegistrationReqDto.ownerName,
			user
		);
	}
}
