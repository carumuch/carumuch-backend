package com.carumuch.capstone.estimate.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateEstimateStatusRequest(
	@NotNull String status
) {
}
