package com.carumuch.capstone.bodyshop.presentation.dto.response;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.Location;

public record BodyShopListResponse(
	Long id,
	String name,
	int acceptCount,
	boolean pickupAvailable,
	Location location
) {
	public BodyShopListResponse(BodyShop bodyShop) {
		this(
			bodyShop.getId(),
			bodyShop.getName(),
			bodyShop.getAcceptCount(),
			bodyShop.isPickupAvailable(),
			bodyShop.getLocation()
		);
	}
}
