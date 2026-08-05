package com.carumuch.capstone.estimate.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record EstimateScrollSlice<T>(
	List<T> content,
	boolean hasNext,
	Long nextCursorId,
	LocalDateTime nextCursorCreatedAt
) {
	public <R> EstimateScrollSlice<R> map(Function<? super T, ? extends R> mapper) {
		List<R> mappedContent = content.stream()
			.map(mapper)
			.collect(Collectors.toList());

		return new EstimateScrollSlice<>(
			mappedContent,
			hasNext,
			nextCursorId,
			nextCursorCreatedAt
		);
	}
}
