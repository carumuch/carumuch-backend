package com.carumuch.capstone.estimate.application.dto;

import java.time.LocalDateTime;

import com.carumuch.capstone.estimate.presentation.dto.request.EstimateScrollRequest;

public record EstimateScrollQuery(
	LocalDateTime cursorCreatedAt,
	Long cursorId,
	int size
) {
	public static EstimateScrollQuery from(EstimateScrollRequest request) {
		return new EstimateScrollQuery(
			request.cursorCreatedAt(),
			request.cursorId(),
			request.size()
		);
	}

	public boolean hasCursor() {
		return cursorCreatedAt != null && cursorId != null;
	}

	public int limitSize() {
		return size + 1;
	}
}
