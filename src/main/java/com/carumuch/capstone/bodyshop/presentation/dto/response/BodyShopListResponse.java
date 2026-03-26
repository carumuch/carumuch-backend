package com.carumuch.capstone.bodyshop.presentation.dto.response;

import com.carumuch.capstone.bodyshop.domain.BodyShop;

public record BodyShopListResponse(
	Long id,
	String name,
	int acceptCount,
	boolean pickupAvailable,
	LocationResponse locationResponse
) {
	public BodyShopListResponse(BodyShop bodyShop) {
		this(
			bodyShop.getId(),
			bodyShop.getName(),
			bodyShop.getAcceptCount(),
			bodyShop.isPickupAvailable(),
			LocationResponse.from(bodyShop.getLocation())
		);
	}
}
