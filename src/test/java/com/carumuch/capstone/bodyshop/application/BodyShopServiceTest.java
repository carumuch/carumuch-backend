package com.carumuch.capstone.bodyshop.application;

import static org.junit.jupiter.api.Assertions.*;

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
import com.carumuch.capstone.bodyshop.presentation.dto.request.LocationRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.UpdateBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopInfoResponse;
import com.carumuch.capstone.common.exception.ForbiddenException;
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

			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailable()
			);

			BodyShop bodyShop = requestDto.toEntity(userId);
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

			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailable()
			);

			BodyShop bodyShop = requestDto.toEntity(userId);
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
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Long userId = 1L;
			ReflectionTestUtils.setField(userFixture, "id", userId);
			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userFixture));

			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailable()
			);

			BodyShop bodyShop = requestDto.toEntity(userId);
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
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Long userId = 1L;
			ReflectionTestUtils.setField(userFixture, "id", userId);
			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userFixture));

			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailable()
			);

			BodyShop bodyShop = requestDto.toEntity(userId);
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
			Long userId = 1L;

			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest requestDto = new RegisterBodyShopRequest(
				bodyShopFixture.getName(),
				bodyShopFixture.getDescription(),
				bodyShopFixture.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShopFixture.getLocation().getSido(),
					bodyShopFixture.getLocation().getSiqungu(),
					bodyShopFixture.getLocation().getBname(),
					bodyShopFixture.getLocation().getJibunAddress(),
					bodyShopFixture.getLocation().getRoadAddress(),
					bodyShopFixture.getLocation().getDetail()
				),
				bodyShopFixture.getLink(),
				bodyShopFixture.isPickupAvailable()
			);

			Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

		    //when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.register(requestDto, userId))
				.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	@DisplayName("공업사 업데이트")
	class Update {
		@Test
		void 공업사_정보를_조회한다() {
		    //given
			Long userId = 1L;
			Long bodyShopId = 500L;
			BodyShop bodyShop = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);

			UpdateBodyShopRequest requestDto = new UpdateBodyShopRequest(
				"이름변경이요",
				bodyShop.getDescription(),
				bodyShop.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShop.getLocation().getSido(),
					bodyShop.getLocation().getSiqungu(),
					bodyShop.getLocation().getBname(),
					bodyShop.getLocation().getJibunAddress(),
					bodyShop.getLocation().getRoadAddress(),
					bodyShop.getLocation().getDetail()
				),
				bodyShop.getLink(),
				bodyShop.isPickupAvailable()
			);

			Mockito.when(bodyShopRepository.findById(bodyShopId))
				.thenReturn(Optional.of(bodyShop));

		    //when
			bodyShopService.update(bodyShopId, requestDto, userId);

		    //then
			Mockito.verify(bodyShopRepository, Mockito.times(1))
				.findById(bodyShopId);
		}

		@Test
		void 공업사를_찾을_수_없으면_예외를_반환한다() {
			//given
			Long userId = 1L;
			Long bodyShopId = 500L;
			BodyShop bodyShop = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);

			UpdateBodyShopRequest requestDto = new UpdateBodyShopRequest(
				"이름변경이요",
				bodyShop.getDescription(),
				bodyShop.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShop.getLocation().getSido(),
					bodyShop.getLocation().getSiqungu(),
					bodyShop.getLocation().getBname(),
					bodyShop.getLocation().getJibunAddress(),
					bodyShop.getLocation().getRoadAddress(),
					bodyShop.getLocation().getDetail()
				),
				bodyShop.getLink(),
				bodyShop.isPickupAvailable()
			);

			Mockito.when(bodyShopRepository.findById(bodyShopId))
				.thenReturn(Optional.empty());

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.update(bodyShopId, requestDto, userId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 공업사에_접근권한이_없다면_예외를_반환환다() {
			//given
			Long userId = 1L;
			Long bodyShopId = 500L;
			BodyShop bodyShop = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);

			UpdateBodyShopRequest requestDto = new UpdateBodyShopRequest(
				"이름변경이요",
				bodyShop.getDescription(),
				bodyShop.getPhoneNumber().getValue(),
				new LocationRequest(
					bodyShop.getLocation().getSido(),
					bodyShop.getLocation().getSiqungu(),
					bodyShop.getLocation().getBname(),
					bodyShop.getLocation().getJibunAddress(),
					bodyShop.getLocation().getRoadAddress(),
					bodyShop.getLocation().getDetail()
				),
				bodyShop.getLink(),
				bodyShop.isPickupAvailable()
			);

			Mockito.when(bodyShopRepository.findById(bodyShopId))
				.thenReturn(Optional.of(bodyShop));

			Long wrongUserId = 2L;

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.update(bodyShopId, requestDto, wrongUserId))
				.isInstanceOf(ForbiddenException.class);
		}

		@Test
		void 공업사_정보를_업데이트한다() {
			//given
			Long userId = 1L;
			Long bodyShopId = 500L;
			BodyShop bodyShop = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);

			UpdateBodyShopRequest requestDto = new UpdateBodyShopRequest(
				"이름변경이요",
				"설명변경이요",
				"010-9876-5432",
				new LocationRequest(
					bodyShop.getLocation().getSido(),
					bodyShop.getLocation().getSiqungu(),
					bodyShop.getLocation().getBname(),
					bodyShop.getLocation().getJibunAddress(),
					bodyShop.getLocation().getRoadAddress(),
					bodyShop.getLocation().getDetail()
				),
				bodyShop.getLink(),
				bodyShop.isPickupAvailable()
			);

			Mockito.when(bodyShopRepository.findById(bodyShopId))
				.thenReturn(Optional.of(bodyShop));

			//when
			bodyShopService.update(bodyShopId, requestDto, userId);

			//then
			assertAll(
				() -> Assertions.assertThat(bodyShop.getName()).isEqualTo(requestDto.name()),
				() -> Assertions.assertThat(bodyShop.getDescription()).isEqualTo(requestDto.description()),
				() -> Assertions.assertThat(bodyShop.getPhoneNumber().getValue()).isEqualTo(requestDto.phoneNumber())
			);
		}
	}

	@Nested
	@DisplayName("공업사 조회 기능")
	class Info {
		@Test
		void 공업사를_조회한다() {
		    //given
			Long bodyShopId = 500L;
			Long userId = 1L;
			BodyShop bodyShop = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			ReflectionTestUtils.setField(bodyShop, "id", bodyShopId);

			Mockito.when(bodyShopRepository.findById(bodyShopId))
				.thenReturn(Optional.of(bodyShop));

		    //when
			bodyShopService.info(bodyShopId);

			//then
			Mockito.verify(bodyShopRepository, Mockito.times(1))
				.findById(bodyShopId);
		}

		@Test
		void 공업사를_찾지_못하면_예외를_반환한다() {
		    //given
			Long bodyShopId = 500L;

			Mockito.when(bodyShopRepository.findById(bodyShopId))
				.thenReturn(Optional.empty());

		    //when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.info(bodyShopId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 공업사_정보를_반환한다() {
			//given
			Long bodyShopId = 500L;
			Long userId = 1L;
			BodyShop bodyShop = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			ReflectionTestUtils.setField(bodyShop, "id", bodyShopId);

			Mockito.when(bodyShopRepository.findById(bodyShopId))
				.thenReturn(Optional.of(bodyShop));

			//when
			BodyShopInfoResponse result = bodyShopService.info(bodyShopId);

			//then
			assertAll(
				() -> Assertions.assertThat(result.id()).isEqualTo(bodyShop.getId()),
				() -> Assertions.assertThat(result.name()).isEqualTo(bodyShop.getName()),
				() -> Assertions.assertThat(result.description()).isEqualTo(bodyShop.getDescription()),
				() -> Assertions.assertThat(result.link()).isEqualTo(bodyShop.getLink()),
				() -> Assertions.assertThat(result.acceptCount()).isEqualTo(bodyShop.getAcceptCount()),
				() -> Assertions.assertThat(result.phoneNumber()).isEqualTo(bodyShop.getPhoneNumber().getValue()),
				() -> Assertions.assertThat(result.pickupAvailable()).isEqualTo(bodyShop.isPickupAvailable()),
				() -> Assertions.assertThat(result.locationResponse().bname()).isEqualTo(bodyShop.getLocation().getBname()),
				() -> Assertions.assertThat(result.locationResponse().sido()).isEqualTo(bodyShop.getLocation().getSido()),
				() -> Assertions.assertThat(result.locationResponse().sigungu()).isEqualTo(bodyShop.getLocation().getSiqungu()),
				() -> Assertions.assertThat(result.locationResponse().roadAddress()).isEqualTo(bodyShop.getLocation().getRoadAddress()),
				() -> Assertions.assertThat(result.locationResponse().jibunAddress()).isEqualTo(bodyShop.getLocation().getJibunAddress()),
				() -> Assertions.assertThat(result.locationResponse().detail()).isEqualTo(bodyShop.getLocation().getDetail())
			);
		}
	}
}
