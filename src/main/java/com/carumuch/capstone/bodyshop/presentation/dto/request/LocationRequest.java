package com.carumuch.capstone.bodyshop.presentation.dto.request;

import com.carumuch.capstone.bodyshop.domain.Location;

import jakarta.validation.constraints.NotBlank;

public record LocationRequest(
	@NotBlank String sido,
	@NotBlank String sigungu,
	@NotBlank String bname,
	@NotBlank String jibunAddress,
	@NotBlank String roadAddress,
	String detail
) {
	public Location toLocation() {
		return new Location(
			sido,
			sigungu,
			bname,
			jibunAddress,
			roadAddress,
			detail
		);
	}
}
