package com.carumuch.capstone.auth.presentation.dto.request;

import jakarta.validation.constraints.Email;

public record RetrieveLoginIdRequest(
	@Email String email
) {
}
