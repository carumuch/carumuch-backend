package com.carumuch.capstone.damage.presentation.dto.request.vehicle;

import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.domain.vehicle.VehicleOwnershipType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterVehicleRequest(
	@NotBlank String licenseNumber,
	@NotBlank String ownershipType,
	@NotBlank String brand,
	@NotNull Integer modelYear,
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
