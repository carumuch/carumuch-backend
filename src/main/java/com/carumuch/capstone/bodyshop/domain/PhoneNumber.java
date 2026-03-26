package com.carumuch.capstone.bodyshop.domain;

import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.carumuch.capstone.common.exception.CustomException;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {
	private static final String PATTERN = "^[0-9\\-\\s]{7,15}$";

	private String value;

	public PhoneNumber(String value) {
		if (value == null || !value.matches(PATTERN)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "올바른 전화번호 형식이 아닙니다.");
		}
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		PhoneNumber that = (PhoneNumber)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
