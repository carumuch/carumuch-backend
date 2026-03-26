package com.carumuch.capstone.bodyshop.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record UpdateBodyShopRequest(
	@NotBlank String name,
	@NotBlank String description,
	@NotBlank String phoneNumber,
	@Valid LocationRequest locationRequest,
	String link,
	Boolean pickupAvailable
) {

}
