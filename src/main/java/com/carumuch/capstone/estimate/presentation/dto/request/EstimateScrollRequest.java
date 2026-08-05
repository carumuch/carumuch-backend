package com.carumuch.capstone.estimate.presentation.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record EstimateScrollRequest(
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime cursorCreatedAt,
	Long cursorId,
	Integer size
) {
	private static final int DEFAULT_SIZE = 10;
	private static final int MAX_SIZE = 100;

	public Integer size() {
		return (size == null || size <= 0 || size > MAX_SIZE) ? DEFAULT_SIZE : size;
	}
}
