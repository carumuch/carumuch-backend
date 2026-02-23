package com.carumuch.capstone.bodyshop.presentation.dto.request;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.Location;
import com.carumuch.capstone.identity.domain.user.User;

import jakarta.validation.constraints.NotBlank;

public record RegisterBodyShopRequest(
	@NotBlank String name,
	@NotBlank String description,
	@NotBlank String phoneNumber,
	Location location,
	String link,
	Boolean pickupAvailability
) {
	public BodyShop toEntity(User user) {
		return new BodyShop(name, location, description, link, phoneNumber, pickupAvailability, user);
	}
}
