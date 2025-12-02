package com.carumuch.capstone.identity.application;

public interface RefreshTokenStore {
	String KEY_PREFIX = "rt:";

	void save(String subject, String token);
	String get(String subject);
	void delete(String subject);
}
