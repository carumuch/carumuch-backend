package com.carumuch.capstone.bodyshop.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;
import com.carumuch.capstone.support.IntegrationSupportTest;
import com.carumuch.capstone.support.fixture.BodyShopFixture;
import com.carumuch.capstone.support.fixture.UserFixture;

@DisplayName("공업사 동시성 통합 테스트")
class BodyShopConcurrencyIntegrationTest extends IntegrationSupportTest {

	@Autowired
	BodyShopRepository bodyShopRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PlatformTransactionManager transactionManager;

	BodyShop bodyShop;

	@BeforeEach
	void setUp() {
		User mechanicUser = userRepository.save(UserFixture.USER_FIXTURE_2.create());
		BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(mechanicUser.getId());
		bodyShop = bodyShopRepository.save(
			new BodyShop(
				bodyShopFixture.getName(),
				bodyShopFixture.getLocation(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getLink(),
				bodyShopFixture.getPhoneNumber(),
				bodyShopFixture.isPickupAvailable(),
				bodyShopFixture.getManagerUserId()
			)
		);
		mechanicUser.assignBodyShop(bodyShop);
	}

	@Nested
	@DisplayName("공업사 수리 이력 증가")
	class IncreaseAcceptCount {

		@Test
		@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
		@Transactional(propagation = Propagation.NOT_SUPPORTED)
		void 오래된_버전으로_수정하면_낙관적_락_예외가_발생한다() {
			TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
			Long bodyShopId = bodyShop.getId();

			BodyShop staleBodyShop = transactionTemplate.execute(status ->
				bodyShopRepository.findById(bodyShopId)
					.orElseThrow(() -> new AssertionError("BodyShop not found"))
			);

			transactionTemplate.executeWithoutResult(status -> {
				BodyShop currentBodyShop = bodyShopRepository.findById(bodyShopId)
					.orElseThrow(() -> new AssertionError("BodyShop not found"));
				currentBodyShop.increaseAcceptCount();
			});

			staleBodyShop.increaseAcceptCount();

			Assertions.assertThatThrownBy(() ->
				transactionTemplate.executeWithoutResult(status -> bodyShopRepository.save(staleBodyShop))
			).isInstanceOf(ObjectOptimisticLockingFailureException.class);
		}
	}
}
