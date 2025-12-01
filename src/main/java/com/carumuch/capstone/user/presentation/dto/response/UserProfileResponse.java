package com.carumuch.capstone.user.presentation.dto.response;

import com.carumuch.capstone.user.domain.User;

public record UserProfileResponse(
	String loginId,
	String email,
	String name
) {
	public static UserProfileResponse from(User user) {
		return new UserProfileResponse(user.getLoginId(), user.getEmail(), user.getName());
	}
}
