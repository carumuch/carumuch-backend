package com.carumuch.capstone.bodyshop.presentation.dto.request;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.PhoneNumber;

import jakarta.validation.constraints.NotBlank;

public record RegisterBodyShopRequest(
	@NotBlank String name,
	@NotBlank String description,
	@NotBlank String phoneNumber,
	LocationRequest locationRequest,
	String link,
	Boolean pickupAvailable
) {
	public BodyShop toEntity(Long managerUserId) {
		return new BodyShop(
			name,
			locationRequest.toLocation(),
			description,
			link,
			new PhoneNumber(phoneNumber),
			pickupAvailable,
			managerUserId
		);
	}
}
