package com.carumuch.capstone.bodyshop.presentation.dto.response;

import com.carumuch.capstone.bodyshop.domain.Location;

public record BodyShopInfoResponse(
	Long id,
	String name,
	Location location,
	String description,
	String phoneNumber,
	String link,
	int acceptCount,
	boolean pickupAvailability
) {

}
