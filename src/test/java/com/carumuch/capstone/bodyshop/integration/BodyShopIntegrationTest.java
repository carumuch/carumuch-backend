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
import com.carumuch.capstone.common.domain.Location;
import com.carumuch.capstone.bodyshop.domain.PhoneNumber;
import com.carumuch.capstone.bodyshop.presentation.dto.request.LocationRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopInfoResponse;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.SearchBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.UpdateBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopListResponse;
import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.exception.ForbiddenException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
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
				bodyShopFixture.isPickupAvailable(),
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

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.register(registerBodyShopRequest, userId))
					.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 이미_공업사를_등록한_사용자라면_예외를_반환한다() {
			//given
			Long userId = mechanicUser.getId();
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest registerBodyShopRequest = new RegisterBodyShopRequest(
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

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.register(registerBodyShopRequest, userId))
				.isInstanceOf(CustomException.class);
		}

		@Test
		void 사용자를_공업사_사용자로_변경한다() {
		    //given
			Long userId = customerUser.getId();
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			RegisterBodyShopRequest registerBodyShopRequest = new RegisterBodyShopRequest(
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

			//when
			Long bodyShopId = bodyShopService.register(registerBodyShopRequest, userId);

			//then
			BodyShop result = bodyShopRepository.findById(bodyShopId)
				.orElseThrow(() -> new AssertionError("BodyShop not found"));

			assertAll(
				() -> Assertions.assertThat(result.getName()).isEqualTo(registerBodyShopRequest.name()),
				() -> Assertions.assertThat(result.getDescription()).isEqualTo(registerBodyShopRequest.description()),
				() -> Assertions.assertThat(result.getPhoneNumber().getValue()).isEqualTo(registerBodyShopRequest.phoneNumber()),
				() -> Assertions.assertThat(result.getLink()).isEqualTo(registerBodyShopRequest.link()),
				() -> Assertions.assertThat(result.getLocation().getSido()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getSido()),
				() -> Assertions.assertThat(result.getLocation().getBname()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getBname()),
				() -> Assertions.assertThat(result.getLocation().getJibunAddress()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getJibunAddress()),
				() -> Assertions.assertThat(result.getLocation().getDetail()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getDetail()),
				() -> Assertions.assertThat(result.getLocation().getSiqungu()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getSiqungu()),
				() -> Assertions.assertThat(result.getLocation().getRoadAddress()).isEqualTo(registerBodyShopRequest.locationRequest().toLocation().getRoadAddress()),
				() -> Assertions.assertThat(result.isPickupAvailable()).isEqualTo(registerBodyShopRequest.pickupAvailable())
			);
		}
	}

	@Nested
	@DisplayName("공업사 업데이트 기능")
	class Update {
		@Test
		void 공업사를_업데이트한다() {

			Long userId = mechanicUser.getId();
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_3.create(userId);
			UpdateBodyShopRequest requestDto = new UpdateBodyShopRequest(
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

			//when
			bodyShopService.update(bodyShop.getId(), requestDto, userId);

			//then
			BodyShop result = bodyShopRepository.findById(bodyShop.getId())
				.orElseThrow(() -> new AssertionError("BodyShop not found"));

			assertAll(
				() -> Assertions.assertThat(result.getName()).isEqualTo(requestDto.name()),
				() -> Assertions.assertThat(result.getDescription()).isEqualTo(requestDto.description()),
				() -> Assertions.assertThat(result.getPhoneNumber().getValue()).isEqualTo(requestDto.phoneNumber()),
				() -> Assertions.assertThat(result.getLink()).isEqualTo(requestDto.link()),
				() -> Assertions.assertThat(result.getLocation().getSido()).isEqualTo(requestDto.locationRequest().toLocation().getSido()),
				() -> Assertions.assertThat(result.getLocation().getBname()).isEqualTo(requestDto.locationRequest().toLocation().getBname()),
				() -> Assertions.assertThat(result.getLocation().getJibunAddress()).isEqualTo(requestDto.locationRequest().toLocation().getJibunAddress()),
				() -> Assertions.assertThat(result.getLocation().getDetail()).isEqualTo(requestDto.locationRequest().toLocation().getDetail()),
				() -> Assertions.assertThat(result.getLocation().getSiqungu()).isEqualTo(requestDto.locationRequest().toLocation().getSiqungu()),
				() -> Assertions.assertThat(result.getLocation().getRoadAddress()).isEqualTo(requestDto.locationRequest().toLocation().getRoadAddress()),
				() -> Assertions.assertThat(result.isPickupAvailable()).isEqualTo(requestDto.pickupAvailable())
			);
		}

		@Test
		void 공업사를_찾을_수_없으면_예외를_반환한다() {
			//given
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(mechanicUser.getId());
			UpdateBodyShopRequest requestDto = new UpdateBodyShopRequest(
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

			Long wrongBodyShopId = 5555L;

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.update(wrongBodyShopId, requestDto, mechanicUser.getId()))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 공업사에_접근권한이_없으면_예외를_반환한다() {
			//given
			Long userId = 9999L;
			BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(userId);
			UpdateBodyShopRequest requestDto = new UpdateBodyShopRequest(
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

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.update(bodyShop.getId(), requestDto, userId))
				.isInstanceOf(ForbiddenException.class);
		}
	}

	@Nested
	@DisplayName("공업사 조회 기능")
	class Info {
		@Test
		void 공업사를_조회한다() {
			//given
			Long bodyShopId = bodyShop.getId();

			//when
			BodyShopInfoResponse result = bodyShopService.info(bodyShopId);

			//then
			assertAll(
				() -> Assertions.assertThat(result.name()).isEqualTo(bodyShop.getName()),
				() -> Assertions.assertThat(result.description()).isEqualTo(bodyShop.getDescription()),
				() -> Assertions.assertThat(result.phoneNumber()).isEqualTo(bodyShop.getPhoneNumber().getValue()),
				() -> Assertions.assertThat(result.link()).isEqualTo(bodyShop.getLink()),
				() -> Assertions.assertThat(result.locationResponse().sido()).isEqualTo(bodyShop.getLocation().getSido()),
				() -> Assertions.assertThat(result.locationResponse().sigungu()).isEqualTo(bodyShop.getLocation().getSiqungu()),
				() -> Assertions.assertThat(result.locationResponse().bname()).isEqualTo(bodyShop.getLocation().getBname()),
				() -> Assertions.assertThat(result.locationResponse().roadAddress()).isEqualTo(bodyShop.getLocation().getRoadAddress()),
				() -> Assertions.assertThat(result.locationResponse().jibunAddress()).isEqualTo(bodyShop.getLocation().getJibunAddress()),
				() -> Assertions.assertThat(result.locationResponse().detail()).isEqualTo(bodyShop.getLocation().getDetail()),
				() -> Assertions.assertThat(result.pickupAvailable()).isEqualTo(bodyShop.isPickupAvailable())
			);
		}

		@Test
		void 공업사를_찾지_못하면_예외를_반환한다() {
			//given
			Long wrongBodyShopId = 9999L;

			//when & then
			Assertions.assertThatThrownBy(() -> bodyShopService.info(wrongBodyShopId))
				.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	@DisplayName("공업사 검색 기능")
	class Search {
		@Test
		void 키워드만으로_공업사를_검색한다() {
			//given
			saveBodyShop("송파 차케어", "서울시", "송파구", true, customerUser.getId());
			saveBodyShop("강남 정비소", "서울시", "강남구", true, customerUser.getId());

			//when
			PagingResponse<BodyShopListResponse> result = bodyShopService.search(
				new SearchBodyShopRequest("차", null, null, null),
				new PagingRequest(1, 10, "createDate")
			);

			//then
			assertAll(
				() -> Assertions.assertThat(result.content())
					.extracting(BodyShopListResponse::name)
					.containsExactlyInAnyOrder(bodyShop.getName(), "송파 차케어"),
				() -> Assertions.assertThat(result.page().totalElements()).isEqualTo(2)
			);
		}

		@Test
		void 키워드와_지역_픽업조건을_조합해_검색한다() {
			//given
			saveBodyShop("송파 픽업 차케어", "서울시", "송파구", true, customerUser.getId());
			saveBodyShop("송파 픽업 불가 차케어", "서울시", "송파구", false, customerUser.getId());
			saveBodyShop("부산 픽업 차케어", "부산시", "해운대구", true, customerUser.getId());

			//when
			PagingResponse<BodyShopListResponse> result = bodyShopService.search(
				new SearchBodyShopRequest("픽업", "서울시", "송파구", true),
				new PagingRequest(1, 10, "createDate")
			);

			//then
			assertAll(
				() -> Assertions.assertThat(result.content()).hasSize(1),
				() -> Assertions.assertThat(result.content().get(0).name()).isEqualTo("송파 픽업 차케어"),
				() -> Assertions.assertThat(result.page().totalElements()).isEqualTo(1)
			);
		}
	}

	private BodyShop saveBodyShop(String name, String sido, String sigungu, boolean pickupAvailable, Long managerUserId) {
		return bodyShopRepository.save(
			new BodyShop(
				name,
				new Location(
					sido,
					sigungu,
					"중앙동",
					sido + " " + sigungu + " 1-1",
					sido + " " + sigungu + "로 1",
					"1층"
				),
				name + " 설명",
				"https:" + name + ".com",
				new PhoneNumber("000-9999-9999"),
				pickupAvailable,
				managerUserId
			)
		);
	}
}
