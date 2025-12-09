package com.carumuch.capstone.support.fixture;

import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.domain.vehicle.VehicleOwnershipType;

public enum VehicleFixture {
	VEHICLE_FIXTURE_1("11가1111", "기아", 2000, "sm3", "홍길동"),
	VEHICLE_FIXTURE_2("22나2222", "현대", 2010, "그랜져", "존도"),
	VEHICLE_FIXTURE_3("33다3333", "아우디", 2020, "A7", "제인도");

	private final String licenseNumber;
	private final String brand;
	private final int modelYear;
	private final String modelName;
	private final String ownerName;

	VehicleFixture(String licenseNumber, String brand, int modelYear, String modelName, String ownerName) {
		this.licenseNumber = licenseNumber;
		this.brand = brand;
		this.modelYear = modelYear;
		this.modelName = modelName;
		this.ownerName = ownerName;
	}

	public Vehicle create() {
		return new Vehicle(licenseNumber, VehicleOwnershipType.PERSONAL, brand, modelYear, modelName, ownerName, UserFixture.USER_FIXTURE_1.create());
	}
}
