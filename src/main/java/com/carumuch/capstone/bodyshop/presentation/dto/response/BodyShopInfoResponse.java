package com.carumuch.capstone.bodyshop.presentation.dto.response;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.presentation.dto.LocationResponse;

public record BodyShopInfoResponse(
	Long id,
	String name,
	LocationResponse locationResponse,
	String description,
	String phoneNumber,
	String link,
	int acceptCount,
	boolean pickupAvailable
) {
	public static BodyShopInfoResponse from(BodyShop bodyShop) {
		return new BodyShopInfoResponse(
			bodyShop.getId(),
			bodyShop.getName(),
			LocationResponse.from(bodyShop.getLocation()),
			bodyShop.getDescription(),
			bodyShop.getPhoneNumber().getValue(),
			bodyShop.getLink(),
			bodyShop.getAcceptCount(),
			bodyShop.isPickupAvailable()
		);
	}
}
