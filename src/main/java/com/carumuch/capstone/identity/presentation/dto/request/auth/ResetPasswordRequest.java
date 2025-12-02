package com.carumuch.capstone.identity.presentation.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
	@NotBlank
	String token,
	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*?_~])[A-Za-z\\d!@#$%^&*?_~]{8,16}$")
	String password
) {
}
