package com.carumuch.capstone.identity.presentation.dto.response.user;

import com.carumuch.capstone.identity.domain.user.User;

public record PublicUserProfileResponse(
	String email,
	String name
) {
	public static PublicUserProfileResponse from(User user) {
		return new PublicUserProfileResponse(user.getEmail(), user.getName());
	}
}
