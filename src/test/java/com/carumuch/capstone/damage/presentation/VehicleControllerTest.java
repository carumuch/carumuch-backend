package com.carumuch.capstone.damage.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.ApiErrorResponse;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.support.RestDocsSupport;
import com.carumuch.capstone.support.fixture.VehicleFixture;
import com.carumuch.capstone.damage.domain.Vehicle;
import com.carumuch.capstone.damage.presentation.dto.request.RegisterVehicleRequest;
import com.carumuch.capstone.damage.presentation.dto.request.UpdateVehicleRequest;
import com.carumuch.capstone.damage.presentation.dto.response.VehicleInfoResponse;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;

class VehicleControllerTest extends RestDocsSupport {

	private static final String BASE_URI = "/vehicles";
	private static final String BASE_TAG = "Damage - Vehicle";
	private static final String BASE_SUCCESS_MESSAGE = "OK";

	@Nested
	@DisplayName("차량 등록 API 테스트")
	class RegisterTest {
		@Test
		void 차량_등록_2XX() throws Exception {
			//given
			Mockito.when(vehicleService.register(any(RegisterVehicleRequest.class), any(User.class)))
				.thenReturn(any(Long.class));

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			RegisterVehicleRequest requestDto = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber().getValue(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				post(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andDo(restDocsHandler.document(
					ResourceDocumentation.resource(ResourceSnippetParameters.builder()
						.tag(BASE_TAG)
						.summary("차량 등록")
						.description("## 차량 등록 기능 \n"
							+ "### 사용법 \n"
							+ "- 필드의 validation을 확인해주세요.\n"
							+ "- 사용자는 차량을 한대만 등록할 수 있습니다."
						)
						.requestSchema(Schema.schema(RegisterVehicleRequest.class.getSimpleName()))
						.requestFields(
							fieldWithPath("licenseNumber").description("차량 번호 양식을 지켜주세요.").type(JsonFieldType.STRING),
							fieldWithPath("ownershipType").description("PERSONAL, CORPORATE, LEASE 중에 선택해야합니다.").type(JsonFieldType.STRING),
							fieldWithPath("brand").description("차량 브렌드 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("modelYear").description("차량 연식입니다.").type(JsonFieldType.NUMBER),
							fieldWithPath("modelName").description("차량명 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("ownerName").description("차량 실 소유자명입니다.").type(JsonFieldType.STRING)
						)
						.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
						.build())
					)
				);
		}

		@Test
		void 차량_등록_4XX_차량_번호_중복() throws Exception {
			//given
			String errorMessage = "이미 등록된 차량 번호입니다.";

			Mockito.doThrow(new CustomException(HttpStatus.CONFLICT, errorMessage))
				.when(vehicleService)
				.register(any(RegisterVehicleRequest.class), any(User.class));

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			RegisterVehicleRequest requestDto = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber().getValue(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				post(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isConflict())
				.andExpect(result -> Assertions.assertInstanceOf(CustomException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.requestSchema(Schema.schema(RegisterVehicleRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 차량_등록_4XX_사용자_차량이_이미_존재() throws Exception {
			//given
			String errorMessage = "이미 등록된 차량이 존재합니다.";

			Mockito.doThrow(new CustomException(HttpStatus.CONFLICT, errorMessage))
				.when(vehicleService)
				.register(any(RegisterVehicleRequest.class), any(User.class));

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			RegisterVehicleRequest requestDto = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber().getValue(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				post(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isConflict())
				.andExpect(result -> Assertions.assertInstanceOf(CustomException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.requestSchema(Schema.schema(RegisterVehicleRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 차량_등록_4XX_올바른_차량_번호가_아닌_경우() throws Exception {
			//given
			String errorMessage = "올바른 차량 번호 형식이 아닙니다.";

			Mockito.doThrow(new CustomException(HttpStatus.BAD_REQUEST, errorMessage))
				.when(vehicleService)
				.register(any(RegisterVehicleRequest.class), any(User.class));

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			RegisterVehicleRequest requestDto = new RegisterVehicleRequest(
				"7가77",
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				post(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> Assertions.assertInstanceOf(CustomException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.requestSchema(Schema.schema(RegisterVehicleRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 차량_등록_4XX_올바른_차량_유형이_아닌_경우() throws Exception {
			//given
			String errorMessage = "올바른 차량 유형이 아닙니다.";

			Mockito.doThrow(new CustomException(HttpStatus.BAD_REQUEST, errorMessage))
				.when(vehicleService)
				.register(any(RegisterVehicleRequest.class), any(User.class));

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			RegisterVehicleRequest requestDto = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber().getValue(),
				"stoleMyFriendCar",
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				post(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> Assertions.assertInstanceOf(CustomException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.requestSchema(Schema.schema(RegisterVehicleRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}
	}

	@Nested
	@DisplayName("차량 수정 기능 API 테스트")
	class Update {
		@Test
		void 차량_수정_2XX() throws Exception {
		    //given
			Vehicle vehicle = VehicleFixture.VEHICLE_FIXTURE_2.create();
			UpdateVehicleRequest requestDto = new UpdateVehicleRequest(
				vehicle.getLicenseNumber().getValue(),
				vehicle.getOwnershipType().name(),
				vehicle.getBrand(),
				vehicle.getModelYear(),
				vehicle.getModelName(),
				vehicle.getOwnerName()
			);

			Mockito.doNothing().when(vehicleService).update(any(UpdateVehicleRequest.class), anyLong());

		    //when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI)
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
						.summary("차량 수정")
						.description("## 차량 수정 기능 \n"
							+ "### 사용법 \n"
							+ "- 필드의 validation을 확인해주세요.\n"
						)
						.requestSchema(Schema.schema(UpdateVehicleRequest.class.getSimpleName()))
						.requestFields(
							fieldWithPath("licenseNumber").description("차량 번호 양식을 지켜주세요.").type(JsonFieldType.STRING),
							fieldWithPath("ownershipType").description("PERSONAL, CORPORATE, LEASE 중에 선택해야합니다.").type(JsonFieldType.STRING),
							fieldWithPath("brand").description("차량 브렌드 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("modelYear").description("차량 연식입니다.").type(JsonFieldType.NUMBER),
							fieldWithPath("modelName").description("차량명 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("ownerName").description("차량 실 소유자명입니다.").type(JsonFieldType.STRING)
						)
						.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
						.build())
					)
				);
		}

		@Test
		void 차량_수정_4XX_차량_번호_중복() throws Exception {
			//given
			String errorMessage = "이미 등록된 차량 번호입니다.";

			Mockito.doThrow(new CustomException(HttpStatus.CONFLICT, errorMessage))
				.when(vehicleService)
				.update(any(UpdateVehicleRequest.class), anyLong());

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			UpdateVehicleRequest requestDto = new UpdateVehicleRequest(
				vehicleFixture.getLicenseNumber().getValue(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isConflict())
				.andExpect(result -> Assertions.assertInstanceOf(CustomException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.requestSchema(Schema.schema(UpdateVehicleRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 차량_수정_4XX_올바른_차량_유형이_아닌_경우() throws Exception {
			//given
			String errorMessage = "올바른 차량 유형이 아닙니다.";

			Mockito.doThrow(new CustomException(HttpStatus.BAD_REQUEST, errorMessage))
				.when(vehicleService)
				.update(any(UpdateVehicleRequest.class), anyLong());

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			UpdateVehicleRequest requestDto = new UpdateVehicleRequest(
				vehicleFixture.getLicenseNumber().getValue(),
				"stoleMyFriendCar",
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> Assertions.assertInstanceOf(CustomException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.requestSchema(Schema.schema(UpdateVehicleRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 차량_수정_4XX_등록된_차량이_없는_경우() throws Exception {
			//given
			String errorMessage = Vehicle.class.getSimpleName() + "을(를) 찾을 수 없습니다.";

			Mockito.doThrow(new NotFoundException(Vehicle.class))
				.when(vehicleService).update(any(UpdateVehicleRequest.class), anyLong());

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			UpdateVehicleRequest requestDto = new UpdateVehicleRequest(
				vehicleFixture.getLicenseNumber().getValue(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI)
					.content(objectMapper.writeValueAsString(requestDto))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertInstanceOf(CustomException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.requestSchema(Schema.schema(UpdateVehicleRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}
	}

	@Nested
	@DisplayName("차량 삭제 기능 API 테스트")
	class Delete {
		@Test
		void 차량_삭제_2XX() throws Exception {
		    //given
			Mockito.doNothing().when(vehicleService).delete(anyLong());

		    //when
			ResultActions actions = mockMvc.perform(
				delete(BASE_URI));

		    //then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data").isEmpty())
				.andDo(restDocsHandler.document(
					ResourceDocumentation.resource(ResourceSnippetParameters.builder()
						.tag(BASE_TAG)
						.summary("차량 삭제")
						.description("## 차량 삭제 기능 \n"
							+ "### 사용법 \n"
							+ "- 사용자가 등록한 차량을 삭제합니다.\n"
						)
						.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
						.build())
				));
		}

		@Test
		void 차량_삭제_4xx_등록된_차량이_없는_경우() throws Exception {
		    //given
			String errorMessage = Vehicle.class.getSimpleName() + "을(를) 찾을 수 없습니다.";

			Mockito.doThrow(new NotFoundException(Vehicle.class))
				.when(vehicleService).delete(anyLong());

		    //when
			ResultActions actions = mockMvc.perform(
				delete(BASE_URI));

		    //then
			actions
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertInstanceOf(NotFoundException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}
	}

	@Nested
	@DisplayName("차량 정보 조회 기능 API 테스트")
	class Info {
		@Test
		void 차량_정보_조회_2XX() throws Exception {
		    //given
			Vehicle vehicle = VehicleFixture.VEHICLE_FIXTURE_1.create();
			ReflectionTestUtils.setField(vehicle, "id", 1L);
			VehicleInfoResponse responseDto = new VehicleInfoResponse(
				vehicle.getId(),
				vehicle.getLicenseNumber().getValue(),
				vehicle.getOwnershipType().name(),
				vehicle.getBrand(),
				vehicle.getModelYear(),
				vehicle.getModelName(),
				vehicle.getOwnerName()
			);

			Mockito.when(vehicleService.info(anyLong()))
				.thenReturn(responseDto);

		    //when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI));

		    //then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data.id").value(responseDto.id()))
				.andExpect(jsonPath("$.data.licenseNumber").value(responseDto.licenseNumber()))
				.andExpect(jsonPath("$.data.ownershipType").value(responseDto.ownershipType()))
				.andExpect(jsonPath("$.data.brand").value(responseDto.brand()))
				.andExpect(jsonPath("$.data.modelYear").value(responseDto.modelYear()))
				.andExpect(jsonPath("$.data.modelName").value(responseDto.modelName()))
				.andExpect(jsonPath("$.data.ownerName").value(responseDto.ownerName()))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.summary("차량 정보 조회")
							.description("## 차량 정보 조회 기능 \n"
								+ "### 설명 \n"
								+ "- 사용자가 등록한 차량의 정보를 조회합니다.\n"
							)
							.responseSchema(Schema.schema(VehicleInfoResponse.class.getSimpleName()))
							.responseFields(
								fieldWithPath("message").description("성공 응답 메세지입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.id").description("차량 식별자입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.licenseNumber").description("차량 번호입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.ownershipType").description("차량의 유형입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.brand").description("차량의 브랜드입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.modelYear").description("차량의 연식입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.modelName").description("차량의 모델명입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.ownerName").description("차량의 실제 소유자명입니다.").type(JsonFieldType.STRING)
							)
							.build()
						)
					)
				);

		}

		@Test
		void 차량_정보_조회_4XX_등록된_차량이_없는_경우() throws Exception {
		    //given
			String errorMessage = Vehicle.class.getSimpleName() + "을(를) 찾을 수 없습니다.";

			Mockito.doThrow(new NotFoundException(Vehicle.class))
				.when(vehicleService).info(anyLong());

		    //when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI));

		    //then
			actions
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertInstanceOf(NotFoundException.class, result.getResolvedException()))
				.andExpect(jsonPath("$.message").value(errorMessage))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}
	}
}