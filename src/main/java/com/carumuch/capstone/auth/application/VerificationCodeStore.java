package com.carumuch.capstone.auth.application;

import com.carumuch.capstone.auth.domain.VerificationPurpose;

public interface VerificationCodeStore {
	void save(VerificationPurpose purpose, String key, String code);
	String get(VerificationPurpose purpose, String key);
	void delete(VerificationPurpose purpose, String key);
}
