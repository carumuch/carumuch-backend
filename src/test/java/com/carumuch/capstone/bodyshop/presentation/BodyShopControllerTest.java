package com.carumuch.capstone.bodyshop.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.presentation.dto.request.LocationRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.UpdateBodyShopRequest;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.ApiErrorResponse;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.support.RestDocsSupport;
import com.carumuch.capstone.support.fixture.BodyShopFixture;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;

class BodyShopControllerTest extends RestDocsSupport {

	private static final String BASE_URI = "/body-shops";
	private static final String BASE_TAG = "Body Shop";

	@Nested
	@DisplayName("공업사 등록 API 테스트")
	class Register {
		@Test
		void 공업사_등록_2XX() throws Exception {
			//given
			Mockito.when(bodyShopService.register(any(RegisterBodyShopRequest.class), anyLong()))
				.thenReturn(1L);

			RegisterBodyShopRequest requestDto = createRegisterBodyShopRequest(1L);

			//when
			ResultActions actions = mockMvc.perform(
				post(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data").value(1L))
				.andDo(restDocsHandler.document(
					ResourceDocumentation.resource(ResourceSnippetParameters.builder()
						.tag(BASE_TAG)
						.summary("공업사 등록")
						.description("## 공업사 등록 기능 \n"
							+ "### 설명 \n"
							+ "- 공업사 사용자로 변환됩니다.\n"
							+ "- 공업사 기본 정보와 위치 정보를 함께 등록합니다.\n"
							+ "- 등록 완료 시 생성된 공업사 식별자를 반환합니다."
						)
						.requestSchema(Schema.schema(RegisterBodyShopRequest.class.getSimpleName()))
						.requestFields(
							fieldWithPath("name").description("공업사 이름입니다.").type(JsonFieldType.STRING),
							fieldWithPath("description").description("공업사 소개입니다.").type(JsonFieldType.STRING),
							fieldWithPath("phoneNumber").description("공업사 연락처입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.sido").description("공업사 소재지 시/도입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.sigungu").description("공업사 소재지 시/군/구입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.bname").description("공업사 소재지 법정동명입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.jibunAddress").description("공업사 지번 주소입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.roadAddress").description("공업사 도로명 주소입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.detail").description("공업사 상세 주소입니다.").type(JsonFieldType.STRING),
							fieldWithPath("link").description("공업사 관련 링크입니다.").type(JsonFieldType.STRING),
							fieldWithPath("pickupAvailable").description("차량 픽업 가능 여부입니다.").type(JsonFieldType.BOOLEAN)
						)
						.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
						.build())
					)
				);
		}

		@Test
		void 공업사_등록_4XX_사용자를_찾지_못한_경우() throws Exception {
			//given
			String errorMessage = "User을(를) 찾을 수 없습니다.";

			Mockito.doThrow(new NotFoundException(com.carumuch.capstone.identity.domain.user.User.class))
				.when(bodyShopService)
				.register(any(RegisterBodyShopRequest.class), anyLong());

			RegisterBodyShopRequest requestDto = createRegisterBodyShopRequest(1L);

			//when
			ResultActions actions = mockMvc.perform(
				post(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertInstanceOf(NotFoundException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
					ResourceDocumentation.resource(ResourceSnippetParameters.builder()
						.tag(BASE_TAG)
						.requestSchema(Schema.schema(RegisterBodyShopRequest.class.getSimpleName()))
						.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
						.build())
					)
				);
		}
	}

	@Nested
	@DisplayName("공업사 수정 API 테스트")
	class Update {
		@Test
		void 공업사_수정_2XX() throws Exception {
			//given
			Long bodyShopId = 1L;
			Mockito.doNothing()
				.when(bodyShopService)
				.update(anyLong(), any(UpdateBodyShopRequest.class), anyLong());

			UpdateBodyShopRequest requestDto = createUpdateBodyShopRequest(1L);

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI + "/{bodyShopId}", bodyShopId)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data").isEmpty())
				.andDo(restDocsHandler.document(
					ResourceDocumentation.resource(ResourceSnippetParameters.builder()
						.tag(BASE_TAG)
						.summary("공업사 수정")
						.description("## 공업사 수정 기능 \n"
							+ "### 사용법 \n"
							+ "- 공업사 기본 정보와 위치 정보를 수정합니다.\n"
							+ "- 수정 성공 시 별도 데이터 없이 성공 메시지만 반환합니다."
						)
						.requestSchema(Schema.schema(UpdateBodyShopRequest.class.getSimpleName()))
						.requestFields(
							fieldWithPath("name").description("공업사 이름입니다.").type(JsonFieldType.STRING),
							fieldWithPath("description").description("공업사 소개입니다.").type(JsonFieldType.STRING),
							fieldWithPath("phoneNumber").description("공업사 연락처입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.sido").description("공업사 소재지 시/도입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.sigungu").description("공업사 소재지 시/군/구입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.bname").description("공업사 소재지 법정동명입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.jibunAddress").description("공업사 지번 주소입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.roadAddress").description("공업사 도로명 주소입니다.").type(JsonFieldType.STRING),
							fieldWithPath("locationRequest.detail").description("공업사 상세 주소입니다.").type(JsonFieldType.STRING),
							fieldWithPath("link").description("공업사 관련 링크입니다.").type(JsonFieldType.STRING),
							fieldWithPath("pickupAvailable").description("차량 픽업 가능 여부입니다.").type(JsonFieldType.BOOLEAN)
						)
						.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
						.build())
					)
				);
		}

		@Test
		void 공업사_수정_4XX_공업사를_찾지_못한_경우() throws Exception {
			//given
			Long bodyShopId = 1L;
			String errorMessage = BodyShop.class.getSimpleName() + "을(를) 찾을 수 없습니다.";

			Mockito.doThrow(new NotFoundException(BodyShop.class))
				.when(bodyShopService)
				.update(anyLong(), any(UpdateBodyShopRequest.class), anyLong());

			UpdateBodyShopRequest requestDto = createUpdateBodyShopRequest(1L);

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI + "/{bodyShopId}", bodyShopId)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertInstanceOf(NotFoundException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
					ResourceDocumentation.resource(ResourceSnippetParameters.builder()
						.tag(BASE_TAG)
						.requestSchema(Schema.schema(UpdateBodyShopRequest.class.getSimpleName()))
						.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
						.build())
					)
				);
		}
	}

	private RegisterBodyShopRequest createRegisterBodyShopRequest(Long managerUserId) {
		BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_1.create(managerUserId);
		return new RegisterBodyShopRequest(
			bodyShopFixture.getName(),
			bodyShopFixture.getDescription(),
			bodyShopFixture.getPhoneNumber().getValue(),
			createLocationRequest(bodyShopFixture),
			bodyShopFixture.getLink(),
			bodyShopFixture.isPickupAvailable()
		);
	}

	private UpdateBodyShopRequest createUpdateBodyShopRequest(Long managerUserId) {
		BodyShop bodyShopFixture = BodyShopFixture.BODY_SHOP_FIXTURE_2.create(managerUserId);
		return new UpdateBodyShopRequest(
			bodyShopFixture.getName(),
			bodyShopFixture.getDescription(),
			bodyShopFixture.getPhoneNumber().getValue(),
			createLocationRequest(bodyShopFixture),
			bodyShopFixture.getLink(),
			bodyShopFixture.isPickupAvailable()
		);
	}

	private LocationRequest createLocationRequest(BodyShop bodyShopFixture) {
		return new LocationRequest(
			bodyShopFixture.getLocation().getSido(),
			bodyShopFixture.getLocation().getSiqungu(),
			bodyShopFixture.getLocation().getBname(),
			bodyShopFixture.getLocation().getJibunAddress(),
			bodyShopFixture.getLocation().getRoadAddress(),
			bodyShopFixture.getLocation().getDetail()
		);
	}
}
