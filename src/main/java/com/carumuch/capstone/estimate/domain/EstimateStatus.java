package com.carumuch.capstone.estimate.domain;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import com.carumuch.capstone.common.exception.CustomException;

public enum EstimateStatus {
	OPEN, CLOSED, PRIVATE;

	public static EstimateStatus from(String value) {
		return Arrays.stream(EstimateStatus.values())
			.filter(state -> state.name().equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "올바른 견적서 상태가 아닙니다."));
	}
}
