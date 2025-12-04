package com.carumuch.capstone.vehicle.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleUpdateReqDto(
	@NotBlank String licenseNumber,
	@NotBlank String ownershipType,
	@NotBlank String brand,
	@NotNull int modelYear,
	@NotBlank String modelName,
	@NotBlank String ownerName
) {
}
