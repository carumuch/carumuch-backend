package com.carumuch.capstone.common.presentation.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public record PagingRequest(
	Integer page,
	Integer size,
	String sort
) {
	private static final int DEFAULT_SIZE = 10;
	private static final int MAX_SIZE = 100;
	private static final String DEFAULT_SORT = "createDate";

	public Integer page() {
		return (page == null || page <= 0) ? 0 : page - 1;
	}

	public Integer size() {
		return (size == null || size <= 0 || size > MAX_SIZE) ? DEFAULT_SIZE : size;
	}

	public String sort() {
		return sort == null ? DEFAULT_SORT : sort;
	}

	public PageRequest toPageRequest() {
		return PageRequest.of(page(), size(), Sort.by(sort()));
	}
}