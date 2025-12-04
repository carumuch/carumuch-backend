package com.carumuch.capstone.vehicle.presentation.dto.request;

import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.domain.VehicleOwnershipType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterVehicleRequest(
	@NotBlank String licenseNumber,
	@NotBlank String ownershipType,
	@NotBlank String brand,
	@NotNull int modelYear,
	@NotBlank String modelName,
	@NotBlank String ownerName
) {
	public Vehicle toEntity(User user) {
		return new Vehicle(
			licenseNumber,
			VehicleOwnershipType.from(this.ownershipType),
			brand,
			modelYear,
			modelName,
			ownerName,
			user
		);
	}
}
