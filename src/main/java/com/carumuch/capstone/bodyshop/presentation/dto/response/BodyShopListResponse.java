package com.carumuch.capstone.bodyshop.presentation.dto.response;

import com.carumuch.capstone.bodyshop.domain.Location;

public record BodyShopListResponse(
	Long id,
	String name,
	int acceptCount,
	boolean pickupAvailability,
	Location location
) {

}
