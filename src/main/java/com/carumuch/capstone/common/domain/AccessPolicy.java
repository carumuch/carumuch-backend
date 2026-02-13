package com.carumuch.capstone.common.domain;

public interface AccessPolicy {
	boolean canAccess(Long userId);
}
