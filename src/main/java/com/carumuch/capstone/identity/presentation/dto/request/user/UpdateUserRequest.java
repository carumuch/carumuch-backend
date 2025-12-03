package com.carumuch.capstone.identity.presentation.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
	@NotBlank String name
) {
}
