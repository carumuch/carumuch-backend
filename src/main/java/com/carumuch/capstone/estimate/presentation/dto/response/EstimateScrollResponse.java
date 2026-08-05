package com.carumuch.capstone.estimate.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.carumuch.capstone.estimate.application.dto.EstimateScrollSlice;

public record EstimateScrollResponse(
	List<EstimateDetailResponse> content,
	boolean hasNext,
	Long nextCursorId,
	LocalDateTime nextCursorCreatedAt
) {
	public static EstimateScrollResponse from(EstimateScrollSlice<EstimateDetailResponse> slice) {
		return new EstimateScrollResponse(
			slice.content(),
			slice.hasNext(),
			slice.nextCursorId(),
			slice.nextCursorCreatedAt()
		);
	}
}
