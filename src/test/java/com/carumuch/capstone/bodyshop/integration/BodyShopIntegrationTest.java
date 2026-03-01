package com.carumuch.capstone.bodyshop.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.carumuch.capstone.bodyshop.application.BodyShopService;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;
import com.carumuch.capstone.bodyshop.presentation.dto.request.LocationRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;
import com.carumuch.capstone.support.IntegrationSupportTest;
import com.carumuch.capstone.support.fixture.BodyShopFixture;
import com.carumuch.capstone.support.fixture.UserFixture;

public class BodyShopIntegrationTest extends IntegrationSupportTest {
	@Autowired
	BodyShopService bodyShopService;
	@Autowired
	BodyShopRepository bodyShopRepository;
	@Autowired
	UserRepository userRepository;

	User mechanicUser;
	User customerUser;
	BodyShop bodyShop;

	@BeforeEach
	void setUp() {
		customerUser = userRepository.save(UserFixture.USER_FIXTURE_1.create());

		User mechanicUserFixture = UserFixture.USER_FIXTURE_2.create();
		mechanicUser = userRepository.save(mechanicUserFixture);

		BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(mechanicUser.getId());
		bodyShop = bodyShopRepository.save(
			new BodyShop(
				bodyShopFixture.getName(),
				bodyShopFixture.getLocation(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getLink(),
				bodyShopFixture.getPhoneNumber(),
				bodyShopFixture.isPickupAvailability(),
				bodyShopFixture.getManagerUserId()
			)
		);
		mechanicUser.assignBodyShop(bodyShop);
	}

	@Nested
	@DisplayName("공업사 등록 기능")
	class Register {

		@Test
		void 사용자를_찾지_못하면_예외를_반환한다() {
			//given
			Long userId = 99999L;
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest registerBodyShopRequest = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.register(registerBodyShopRequest, userId))
					.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 사용자를_공업사_사용자로_변경한다() {
		    //given
			Long userId = customerUser.getId();
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest registerBodyShopRequest = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);

		    //when
			bodyShopService.register(registerBodyShopRequest, userId);

		    //then
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new AssertionError("User not found"));
			Assertions.assertThat(user.isMechanic()).isTrue();
		}

		@Test
		void 공업사를_등록한다() {
			//given
			Long userId = customerUser.getId();
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest registerBodyShopRequest = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);

			//when
			bodyShopService.register(registerBodyShopRequest, userId);

			//then
			BodyShop result = bodyShopRepository.findById(bodyShop.getId())
				.orElseThrow(() -> new AssertionError("BodyShop not found"));

			assertAll(
				() -> Assertions.assertThat(result.getName()).isEqualTo(registerBodyShopRequest.name()),
				() -> Assertions.assertThat(result.getDescription()).isEqualTo(registerBodyShopRequest.description()),
				() -> Assertions.assertThat(result.getPhoneNumber()).isEqualTo(registerBodyShopRequest.phoneNumber()),
				() -> Assertions.assertThat(result.getLink()).isEqualTo(registerBodyShopRequest.link()),
				() -> Assertions.assertThat(result.getLocation().getSido()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getSido()),
				() -> Assertions.assertThat(result.getLocation().getBname()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getBname()),
				() -> Assertions.assertThat(result.getLocation().getJibunAddress()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getJibunAddress()),
				() -> Assertions.assertThat(result.getLocation().getDetail()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getDetail()),
				() -> Assertions.assertThat(result.getLocation().getSiqungu()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getSiqungu()),
				() -> Assertions.assertThat(result.getLocation().getRoadAddress()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getRoadAddress()),
				() -> Assertions.assertThat(result.isPickupAvailability()).isEqualTo(registerBodyShopRequest.pickupAvailability())
			);
		}
	}
}
