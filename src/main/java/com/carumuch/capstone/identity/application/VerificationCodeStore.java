package com.carumuch.capstone.identity.application;

import com.carumuch.capstone.identity.domain.auth.VerificationPurpose;

public interface VerificationCodeStore {
	void save(VerificationPurpose purpose, String key, String code);
	String get(VerificationPurpose purpose, String key);
	void delete(VerificationPurpose purpose, String key);
}
