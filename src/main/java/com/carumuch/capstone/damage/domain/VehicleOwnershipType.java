package com.carumuch.capstone.damage.domain;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import com.carumuch.capstone.common.exception.CustomException;

public enum VehicleOwnershipType {
	PERSONAL, CORPORATE, LEASE;

	public static VehicleOwnershipType from(String value) {
		return Arrays.stream(VehicleOwnershipType.values())
			.filter(state -> state.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "올바른 차량 유형이 아닙니다."));
	}
}
