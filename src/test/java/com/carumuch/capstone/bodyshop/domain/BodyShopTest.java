package com.carumuch.capstone.bodyshop.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.carumuch.capstone.support.fixture.BodyShopFixture;

class BodyShopTest {

	@Test
	@DisplayName("수리 이력을 증가시킨다")
	void 수리_이력을_증가시킨다() {
		BodyShop bodyShop = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(1L);

		bodyShop.increaseAcceptCount();

		Assertions.assertThat(bodyShop.getAcceptCount()).isEqualTo(1);
	}
}
