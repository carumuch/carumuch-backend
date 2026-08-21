package com.carumuch.capstone.estimate.presentation.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import com.carumuch.capstone.common.exception.CustomException;

public record EstimateScrollRequest(
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime cursorCreatedAt,
	Long cursorId,
	Integer size
) {
	private static final int DEFAULT_SIZE = 10;
	private static final int MAX_SIZE = 100;
	private static final String INVALID_CURSOR_MESSAGE = "cursorCreatedAt과 cursorId는 함께 전달해야 합니다.";

	public Integer size() {
		return (size == null || size <= 0 || size > MAX_SIZE) ? DEFAULT_SIZE : size;
	}

	public void validateCursorPair() {
		boolean hasCursorCreatedAt = cursorCreatedAt != null;
		boolean hasCursorId = cursorId != null;

		if (hasCursorCreatedAt != hasCursorId) {
			throw new CustomException(HttpStatus.BAD_REQUEST, INVALID_CURSOR_MESSAGE);
		}
	}
}
