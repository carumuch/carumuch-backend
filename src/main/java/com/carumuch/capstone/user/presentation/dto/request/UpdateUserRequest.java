package com.carumuch.capstone.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
	@NotBlank String name
) {
}
