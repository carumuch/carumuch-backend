package com.carumuch.capstone.identity.presentation.dto.request.auth;

import jakarta.validation.constraints.Email;

public record RetrieveLoginIdRequest(
	@Email String email
) {
}
