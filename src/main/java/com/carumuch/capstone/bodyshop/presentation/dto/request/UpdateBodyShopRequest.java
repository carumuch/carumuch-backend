package com.carumuch.capstone.bodyshop.presentation.dto.request;

import com.carumuch.capstone.bodyshop.domain.Location;
import jakarta.validation.constraints.NotBlank;

public record UpdateBodyShopRequest(
	@NotBlank String name,
	@NotBlank String description,
	@NotBlank String phoneNumber,
	Location location,
	String link,
	Boolean pickupAvailable
) {

}
