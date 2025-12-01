package com.carumuch.capstone.auth.presentation.dto.response;

public record AuthenticateUserResponse(
        String accessToken,
        String refreshToken
) {
}
