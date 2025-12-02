package com.carumuch.capstone.identity.presentation.dto.response.auth;

public record AuthenticateUserResponse(
        String accessToken,
        String refreshToken
) {
}
