package com.carumuch.capstone.user.presentation.dto.response;

import com.carumuch.capstone.user.domain.User;

public record PublicUserProfileResponse(
	String email,
	String name
) {
	public static PublicUserProfileResponse from(User user) {
		return new PublicUserProfileResponse(user.getEmail(), user.getName());
	}
}
