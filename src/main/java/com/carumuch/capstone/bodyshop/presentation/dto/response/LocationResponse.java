package com.carumuch.capstone.bodyshop.presentation.dto.response;

import com.carumuch.capstone.bodyshop.domain.Location;

public record LocationResponse(
	String sido,
	String sigungu,
	String bname,
	String jibunAddress,
	String roadAddress,
	String detail
) {
	public static LocationResponse from(Location location) {
		return new LocationResponse(
			location.getSido(),
			location.getSiqungu(),
			location.getBname(),
			location.getJibunAddress(),
			location.getRoadAddress(),
			location.getDetail()
		);
	}
}
