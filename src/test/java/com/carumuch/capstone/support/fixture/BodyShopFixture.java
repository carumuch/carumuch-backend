package com.carumuch.capstone.support.fixture;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.Location;
import com.carumuch.capstone.bodyshop.domain.PhoneNumber;

public enum BodyShopFixture {

	BODY_SHOP_FIXTURE_1(
		"차박고 차량 수리 전문",
		"확실하게 처리해드립니다.",
		"https:test.com",
		"000-1234-1234",
		true,
		new Location(
			"서울시",
			"송파구",
			"신천동",
			"서울 송파구 신천동 29-1",
			"서울 송파구 올림픽1로 300",
			"1층"
		)
	),
	BODY_SHOP_FIXTURE_2(
		"수리수리 차수리",
		"수리라면 자신있습니다.",
		"https:test2.com",
		"000-2345-2345",
		true,
		new Location(
			"서울시",
			"송파구",
			"신천동",
			"서울 송파구 신천동 29-2",
			"서울 송파구 올림픽2로 300",
			"2층"
		)
	),
	BODY_SHOP_FIXTURE_3(
		"가성비 왕 공업사",
		"손해보면서 장사합니다. 가성비 믿어주세요",
		"https:test3.com",
		"000-3456-3456",
		false,
		new Location(
			"서울시",
			"송파구",
			"신천동",
			"서울 송파구 신천동 29-3",
			"서울 송파구 올림픽3로 300",
			"3층"
		)
	);

	private final String name;
	private final String description;
	private final String link;
	private final String phoneNumber;
	private final boolean pickupAvailability;
	private final Location location;

	BodyShopFixture(String name, String description, String link, String phoneNumber, boolean pickupAvailability, Location location) {
		this.name = name;
		this.description = description;
		this.link = link;
		this.phoneNumber = phoneNumber;
		this.pickupAvailability = pickupAvailability;
		this.location = location;
	}

	public BodyShop create(Long managerUserId) {
		return new BodyShop(
			name,
			location,
			description,
			link,
			new PhoneNumber(phoneNumber),
			pickupAvailability,
			managerUserId
		);
	}
}
