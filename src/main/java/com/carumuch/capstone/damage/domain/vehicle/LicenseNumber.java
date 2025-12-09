package com.carumuch.capstone.damage.domain.vehicle;

import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.carumuch.capstone.common.exception.CustomException;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class LicenseNumber {
	private static final String PATTERN = "^(?:\\d{2}|\\d{3})[가-힣]\\d{4}$";

	private String value;

	public LicenseNumber(String value) {
		if (value == null || !value.matches(PATTERN)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "올바른 차량 번호 형식이 아닙니다.");
		}
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		LicenseNumber that = (LicenseNumber)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}

