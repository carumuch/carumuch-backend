package com.carumuch.capstone.support.fixture;

import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.RepairRegion;

public enum DamageReportFixture {
	DAMAGE_REPORT_FIXTURE_1("소화전을 박았습니다.", new RepairRegion("서울시", "송파구"), false, "http://s3.image.test1"),
	DAMAGE_REPORT_FIXTURE_2("야구공에 맞았습니다.", new RepairRegion("서울시", "강남구"), true, "http://s3.image.test2"),
	DAMAGE_REPORT_FIXTURE_3("아내가 주차장 벽을 긁었습니다.", new RepairRegion("경기도", "고양시"), false, "http://s3.image.test3");

	private final String description;
	private final RepairRegion preferredRepairRegion;
	private final boolean isPickupRequired;
	private final String imagePath;

	DamageReportFixture(String description, RepairRegion preferredRepairRegion, boolean isPickupRequired, String imagePath) {
		this.description = description;
		this.preferredRepairRegion = preferredRepairRegion;
		this.isPickupRequired = isPickupRequired;
		this.imagePath = imagePath;
	}

	public DamageReport create() {
		return new DamageReport(description, preferredRepairRegion, isPickupRequired, imagePath, VehicleFixture.VEHICLE_FIXTURE_1.create());
	}
}
