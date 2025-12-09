package com.carumuch.capstone.damage.domain.report;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RepairRegion {
	private String sido;
	private String sigungu;

	public RepairRegion(String sido, String sigungu) {
		this.sido = sido;
		this.sigungu = sigungu;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		RepairRegion that = (RepairRegion)o;
		return Objects.equals(sido, that.sido) && Objects.equals(sigungu, that.sigungu);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sido, sigungu);
	}
}
