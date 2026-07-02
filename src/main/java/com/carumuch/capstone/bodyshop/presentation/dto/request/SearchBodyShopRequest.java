package com.carumuch.capstone.bodyshop.presentation.dto.request;

public record SearchBodyShopRequest(
	String keyword,
	String sido,
	String sigungu,
	Boolean pickupAvailable
) {
}
