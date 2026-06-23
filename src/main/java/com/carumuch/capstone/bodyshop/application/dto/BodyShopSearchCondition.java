package com.carumuch.capstone.bodyshop.application.dto;

import com.carumuch.capstone.bodyshop.presentation.dto.request.SearchBodyShopRequest;

public record BodyShopSearchCondition(
	String keyword,
	String sido,
	String sigungu,
	Boolean pickupAvailable
) {
	public static BodyShopSearchCondition from(SearchBodyShopRequest request) {
		return new BodyShopSearchCondition(
			request.keyword(),
			request.sido(),
			request.sigungu(),
			request.pickupAvailable()
		);
	}
}
