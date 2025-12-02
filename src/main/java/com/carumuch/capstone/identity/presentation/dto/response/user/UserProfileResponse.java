package com.carumuch.capstone.identity.presentation.dto.response.user;

import com.carumuch.capstone.identity.domain.user.User;

public record UserProfileResponse(
	String loginId,
	String email,
	String name
) {
	public static UserProfileResponse from(User user) {
		return new UserProfileResponse(user.getLoginId(), user.getEmail(), user.getName());
	}
}
