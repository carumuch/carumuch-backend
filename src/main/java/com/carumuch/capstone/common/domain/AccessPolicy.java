package com.carumuch.capstone.common.domain;

import com.carumuch.capstone.identity.domain.user.User;

public interface AccessPolicy {
	boolean canAccess(User user);
}
