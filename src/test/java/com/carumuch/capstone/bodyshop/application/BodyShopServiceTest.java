package com.carumuch.capstone.bodyshop.application;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;
import com.carumuch.capstone.support.fixture.BodyShopFixture;
import com.carumuch.capstone.support.fixture.UserFixture;

@ExtendWith(SpringExtension.class)
class BodyShopServiceTest {

	@InjectMocks
	BodyShopService bodyShopService;

	@Mock
	BodyShopRepository bodyShopRepository;

	@Mock
	UserRepository userRepository;

	@Nested
	@DisplayName("공업사 등록")
	class Register {
		@Test
		void 공업사를_저장한다() {
		    //given
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Long userId = 1L;
			ReflectionTestUtils.setField(userFixture, "id", userId);
			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userFixture));

			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create();
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				bodyShopFixture.getLocation(),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);

			BodyShop bodyShop = requestDto.toEntity(userFixture);
			Long bodyShopId = 400L;
			ReflectionTestUtils.setField(bodyShop, "id", bodyShopId);
			Mockito.when(bodyShopRepository.save(Mockito.any(BodyShop.class))).thenReturn(bodyShop);

		    //when
			bodyShopService.register(requestDto, userId);

			//then
			Mockito.verify(bodyShopRepository, Mockito.times(1))
				.save(Mockito.any(BodyShop.class));
		}

		@Test
		void 공업사_PK를_반환한다() {
			//given
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Long userId = 1L;
			ReflectionTestUtils.setField(userFixture, "id", userId);
			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userFixture));

			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create();
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				bodyShopFixture.getLocation(),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);

			BodyShop bodyShop = requestDto.toEntity(userFixture);
			Long bodyShopId = 400L;
			ReflectionTestUtils.setField(bodyShop, "id", bodyShopId);
			Mockito.when(bodyShopRepository.save(Mockito.any(BodyShop.class))).thenReturn(bodyShop);

			//when
			Long result = bodyShopService.register(requestDto, userId);

			//then
			Assertions.assertThat(result).isEqualTo(bodyShopId);
		}

		@Test
		void 사용자를_조회한다() {
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create();
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				bodyShopFixture.getLocation(),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Long userId = 1L;
			ReflectionTestUtils.setField(userFixture, "id", userId);
			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userFixture));

			BodyShop bodyShop = requestDto.toEntity(userFixture);
			Long bodyShopId = 400L;
			ReflectionTestUtils.setField(bodyShop, "id", bodyShopId);
			Mockito.when(bodyShopRepository.save(Mockito.any(BodyShop.class))).thenReturn(bodyShop);

			//when
			bodyShopService.register(requestDto, userId);

			//then
			Mockito.verify(userRepository, Mockito.times(1))
				.findById(userId);
		}

		@Test
		void 사용자를_공업사_직원으로_변경한다() {
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create();
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				bodyShopFixture.getLocation(),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Long userId = 1L;
			ReflectionTestUtils.setField(userFixture, "id", userId);
			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userFixture));

			BodyShop bodyShop = requestDto.toEntity(userFixture);
			Long bodyShopId = 400L;
			ReflectionTestUtils.setField(bodyShop, "id", bodyShopId);
			Mockito.when(bodyShopRepository.save(Mockito.any(BodyShop.class))).thenReturn(bodyShop);

			//when
			bodyShopService.register(requestDto, userId);

			//then
			Assertions.assertThat(userFixture.isMechanic()).isTrue();
		}

		@Test
		void 사용자를_찾지_못하면_예외를_반환한다() {
		    //given
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create();
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber(),
				bodyShopFixture.getLocation(),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailability()
			);
			Long userId = 1L;
			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

		    //when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.register(requestDto, userId))
				.isInstanceOf(NotFoundException.class);
		}
	}

}