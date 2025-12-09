package com.carumuch.capstone.damage.presentation.dto.request.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateVehicleRequest(
	@NotBlank String licenseNumber,
	@NotBlank String ownershipType,
	@NotBlank String brand,
	@NotNull Integer modelYear,
	@NotBlank String modelName,
	@NotBlank String ownerName
) {
}
