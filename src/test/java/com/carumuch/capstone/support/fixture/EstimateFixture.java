package com.carumuch.capstone.support.fixture;

import java.util.Set;

import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateStatus;

public enum EstimateFixture {
	ESTIMATE_FIXTURE_1(
		10_000,
		Set.of("프론트휠 (좌)", "프론트도어 (좌)", "사이드미러 (좌)"),
		EstimateStatus.OPEN,
		"http://test-image1.com"
	),
	ESTIMATE_FIXTURE_2(
		20_000,
		Set.of("프론트휠 (우)", "프론트도어 (우)", "사이드미러 (우)"),
		EstimateStatus.CLOSED,
		"http://test-image2.com"
	),
	ESTIMATE_FIXTURE_3(
		30_000,
		Set.of("백휠 (우)", "백도어 (우)", "사이드미러 (좌)"),
		EstimateStatus.PRIVATE,
		"http://test-image3.com"
	);

	private final Integer repairCost;
	private final Set<String> repairParts;
	private final EstimateStatus estimateStatus;
	private final String imagePath;

	EstimateFixture(
		Integer repairCost,
		Set<String> repairParts,
		EstimateStatus estimateStatus,
		String imagePath
	) {
		this.repairCost = repairCost;
		this.repairParts = repairParts;
		this.estimateStatus = estimateStatus;
		this.imagePath = imagePath;
	}

	public Estimate create() {
		return new Estimate(
			repairCost,
			repairParts,
			estimateStatus,
			imagePath,
			DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create()
		);
	}
}
