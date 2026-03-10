package com.carumuch.capstone.bodyshop.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateBodyShopRequest(
	@NotBlank String name,
	@NotBlank String description,
	@NotBlank String phoneNumber,
	LocationRequest locationRequest,
	String link,
	Boolean pickupAvailable
) {

}
