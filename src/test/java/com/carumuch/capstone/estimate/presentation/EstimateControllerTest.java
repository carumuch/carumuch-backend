package com.carumuch.capstone.estimate.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.ApiErrorResponse;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.presentation.dto.request.SearchEstimateRequest;
import com.carumuch.capstone.estimate.presentation.dto.request.UpdateEstimateStatusRequest;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResponse;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.support.RestDocsSupport;
import com.carumuch.capstone.support.fixture.EstimateFixture;
import com.carumuch.capstone.support.fixture.UserFixture;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class EstimateControllerTest extends RestDocsSupport {

	private static final String BASE_URI = "/estimates";
	private static final String BASE_TAG = "Estimate";

	@Nested
	@DisplayName("견적서 상세 조회 API 테스트")
	class FindEstimateDetail {
		@Test
		void 견적서_상세_조회_2XX() throws Exception {
			//given
			Long estimateId = 300L;
			Estimate estimate = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			ReflectionTestUtils.setField(estimate, "id", estimateId);
			ReflectionTestUtils.setField(estimate, "createDate", LocalDateTime.now());

			EstimateDetailResponse responseDto = new EstimateDetailResponse(estimate);

			Mockito.when(estimateService.findEstimateDetail(estimateId))
				.thenReturn(responseDto);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI + "/{estimateId}", estimateId));

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))

				// 견적서 정보
				.andExpect(jsonPath("$.data.estimateId").value(responseDto.estimateId()))
				.andExpect(jsonPath("$.data.repairCost").value(responseDto.repairCost()))
				.andExpect(jsonPath("$.data.repairParts").isArray())
				.andExpect(jsonPath("$.data.estimateStatus").value(responseDto.estimateStatus()))
				.andExpect(jsonPath("$.data.imagePath").value(responseDto.imagePath()))

				// 사고 레포트 정보
				.andExpect(jsonPath("$.data.damageReportInfo.description").value(responseDto.damageReportInfo().description()))
				.andExpect(jsonPath("$.data.damageReportInfo.preferredRepairSido").value(responseDto.damageReportInfo().preferredRepairSido()))
				.andExpect(jsonPath("$.data.damageReportInfo.preferredRepairSigungu").value(responseDto.damageReportInfo().preferredRepairSigungu()))
				.andExpect(jsonPath("$.data.damageReportInfo.isPickupRequired").value(responseDto.damageReportInfo().isPickupRequired()))

				// 차량 정보
				.andExpect(jsonPath("$.data.vehicleInfo.brand").value(responseDto.vehicleInfo().brand()))
				.andExpect(jsonPath("$.data.vehicleInfo.licenseNumber").value(responseDto.vehicleInfo().licenseNumber()))
				.andExpect(jsonPath("$.data.vehicleInfo.modelName").value(responseDto.vehicleInfo().modelName()))
				.andExpect(jsonPath("$.data.vehicleInfo.modelYear").value(responseDto.vehicleInfo().modelYear()))
				.andExpect(jsonPath("$.data.vehicleInfo.ownerName").value(responseDto.vehicleInfo().ownerName()))
				.andExpect(jsonPath("$.data.vehicleInfo.ownershipType").value(responseDto.vehicleInfo().ownershipType()))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.summary("견적서 상세 조회")
							.description("## 견적서 상세 조회 기능 \n"
								+ "### 설명 \n"
								+ "- 사고 레포트에 의해 생성된 견적서 정보를 조회합니다.\n"
							)
							.responseSchema(Schema.schema(EstimateDetailResponse.class.getSimpleName()))
							.responseFields(
								fieldWithPath("message").description("성공 응답 메세지입니다.").type(JsonFieldType.STRING),
								// 견적서 정보
								fieldWithPath("data.estimateId").description("견적서 식별자입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.repairCost").description("사고 견적 분석 금액입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.repairParts").description("사고 수리 예상 부위입니다.").type(JsonFieldType.ARRAY),
								fieldWithPath("data.estimateStatus").description("견적서의 상태입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.imagePath").description("견적서의 사고 분석 사진 주소입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.createdAt").description("견적서 생성일 입니다.").type(JsonFieldType.STRING),

								// 사고 레포트 정보
								fieldWithPath("data.damageReportInfo.description").description("사용자가 기술한 사고 레포트의 내용입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.damageReportInfo.preferredRepairSido").description("수리 희망 시/도 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.damageReportInfo.preferredRepairSigungu").description("수리 희망 시/군/구 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.damageReportInfo.isPickupRequired").description("사고 수리 픽업 희망 유무입니다.").type(JsonFieldType.BOOLEAN),

								// 차량 정보
								fieldWithPath("data.vehicleInfo.brand").description("차량의 브랜드입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.licenseNumber").description("차량의 번호입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.modelName").description("차량의 모델명입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.modelYear").description("차량의 연입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.vehicleInfo.ownerName").description("차량의 실소유자명입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.ownershipType").description("차량의 유형입니다.").type(JsonFieldType.STRING)
							)
							.build()
						)
					)
				);
		}

		@Test
		void 견적서_상세_조회_4XX_견적서를_찾지_못한_경우() throws Exception {
			String errorMessage = Estimate.class.getSimpleName() + "을(를) 찾을 수 없습니다.";
			Long estimateId = 300L;

			Mockito.doThrow(new NotFoundException(Estimate.class))
				.when(estimateService).findEstimateDetail(estimateId);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI+ "/{estimateId}", estimateId)
			);

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
	@DisplayName("사고레포트를_통한_견적서 상세 조회 API 테스트")
	class FindEstimateDetailByDamageReportId {
		@Test
		void 사고레포트를_통한_견적서_상세_조회_2XX() throws Exception {
			//given
			Long damageReportId = 100L;

			Long estimateId = 300L;
			Estimate estimate = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			ReflectionTestUtils.setField(estimate, "id", estimateId);
			ReflectionTestUtils.setField(estimate, "createDate", LocalDateTime.now());

			EstimateDetailResponse responseDto = new EstimateDetailResponse(estimate);

			Mockito.when(estimateService.findEstimateDetailByDamageReportId(damageReportId))
				.thenReturn(responseDto);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI)
					.param("damageReportId", damageReportId.toString())
			);

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))

				// 견적서 정보
				.andExpect(jsonPath("$.data.estimateId").value(responseDto.estimateId()))
				.andExpect(jsonPath("$.data.repairCost").value(responseDto.repairCost()))
				.andExpect(jsonPath("$.data.repairParts").isArray())
				.andExpect(jsonPath("$.data.estimateStatus").value(responseDto.estimateStatus()))
				.andExpect(jsonPath("$.data.imagePath").value(responseDto.imagePath()))

				// 사고 레포트 정보
				.andExpect(jsonPath("$.data.damageReportInfo.description").value(responseDto.damageReportInfo().description()))
				.andExpect(jsonPath("$.data.damageReportInfo.preferredRepairSido").value(responseDto.damageReportInfo().preferredRepairSido()))
				.andExpect(jsonPath("$.data.damageReportInfo.preferredRepairSigungu").value(responseDto.damageReportInfo().preferredRepairSigungu()))
				.andExpect(jsonPath("$.data.damageReportInfo.isPickupRequired").value(responseDto.damageReportInfo().isPickupRequired()))

				// 차량 정보
				.andExpect(jsonPath("$.data.vehicleInfo.brand").value(responseDto.vehicleInfo().brand()))
				.andExpect(jsonPath("$.data.vehicleInfo.licenseNumber").value(responseDto.vehicleInfo().licenseNumber()))
				.andExpect(jsonPath("$.data.vehicleInfo.modelName").value(responseDto.vehicleInfo().modelName()))
				.andExpect(jsonPath("$.data.vehicleInfo.modelYear").value(responseDto.vehicleInfo().modelYear()))
				.andExpect(jsonPath("$.data.vehicleInfo.ownerName").value(responseDto.vehicleInfo().ownerName()))
				.andExpect(jsonPath("$.data.vehicleInfo.ownershipType").value(responseDto.vehicleInfo().ownershipType()))
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.summary("사고레포트를 통한 견적서 상세 조회")
							.description("## 사고레포트를 통한 견적서 상세 조회 기능 \n"
								+ "### 설명 \n"
								+ "- 사고 레포트에 의해 생성된 견적서 정보를 조회합니다.\n"
							)
							.responseSchema(Schema.schema(EstimateDetailResponse.class.getSimpleName()))
							.responseFields(
								fieldWithPath("message").description("성공 응답 메세지입니다.").type(JsonFieldType.STRING),
								// 견적서 정보
								fieldWithPath("data.estimateId").description("견적서 식별자입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.repairCost").description("사고 견적 분석 금액입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.repairParts").description("사고 수리 예상 부위입니다.").type(JsonFieldType.ARRAY),
								fieldWithPath("data.estimateStatus").description("견적서의 상태입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.imagePath").description("견적서의 사고 분석 사진 주소입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.createdAt").description("견적서 생성일 입니다.").type(JsonFieldType.STRING),

								// 사고 레포트 정보
								fieldWithPath("data.damageReportInfo.description").description("사용자가 기술한 사고 레포트의 내용입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.damageReportInfo.preferredRepairSido").description("수리 희망 시/도 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.damageReportInfo.preferredRepairSigungu").description("수리 희망 시/군/구 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.damageReportInfo.isPickupRequired").description("사고 수리 픽업 희망 유무입니다.").type(JsonFieldType.BOOLEAN),

								// 차량 정보
								fieldWithPath("data.vehicleInfo.brand").description("차량의 브랜드입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.licenseNumber").description("차량의 번호입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.modelName").description("차량의 모델명입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.modelYear").description("차량의 연입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.vehicleInfo.ownerName").description("차량의 실소유자명입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.vehicleInfo.ownershipType").description("차량의 유형입니다.").type(JsonFieldType.STRING)
							)
							.build()
						)
					)
				);
		}

		@Test
		void 사고레포트를_통한_견적서_상세_조회_4XX_견적서를_찾지_못한_경우() throws Exception {
			String errorMessage = Estimate.class.getSimpleName() + "을(를) 찾을 수 없습니다.";

			Long damageReportId = 100L;

			Mockito.doThrow(new NotFoundException(Estimate.class))
				.when(estimateService).findEstimateDetailByDamageReportId(damageReportId);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI)
					.param("damageReportId", damageReportId.toString())
			);

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
	@DisplayName("견적서 상태 변경 API 테스트")
	class UpdateEstimateStatus {
		@Test
		void 견적서_상태_변경_기능_2XX() throws Exception {
		    //given
			Long estimateId = 300L;

			String changeEstimateStatus = EstimateStatus.PRIVATE.name();
			UpdateEstimateStatusRequest requestDto = new UpdateEstimateStatusRequest(changeEstimateStatus);

			Mockito.doNothing().when(estimateService).changeStatus(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
		    
		    //when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI + "/{estimateId}/status", estimateId)
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
							.summary("견적서 상태 변경")
							.description("## 견적서 상태 변경 기능 \n"
								+ "### 사용법 \n"
								+ "- PRIVATE, OPEN 중 선택하여, 작성해주세요. \n"
								+ "- 대문자, 소문자 모두 수용합니다."
								+ "- 이미 매칭된 견적서는 상태를 수정할 수 없습니다."
							)
							.requestSchema(Schema.schema(UpdateEstimateStatusRequest.class.getSimpleName()))
							.requestFields(
								fieldWithPath("status").description("변경할 견적서의 상태입니다. (PRIVATE, OPEN 중에 선택해야합니다.)").type(JsonFieldType.STRING)
							)
							.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 견적서_상태_변경_기능_4XX_견적서를_찾을_수_없는_경우() throws Exception {
			//given
			String errorMessage = Estimate.class.getSimpleName() + "을(를) 찾을 수 없습니다.";

			Long estimateId = 300L;
			String changeEstimateStatus = EstimateStatus.PRIVATE.name();
			UpdateEstimateStatusRequest requestDto = new UpdateEstimateStatusRequest(changeEstimateStatus);

			Mockito.doThrow(new NotFoundException(Estimate.class))
				.when(estimateService).changeStatus(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI + "/{estimateId}/status", estimateId)
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
							.requestSchema(Schema.schema(UpdateEstimateStatusRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 견적서_상태_변경_기능_4XX_이미_매칭_완료된_견적서인_경우() throws Exception {
			//given
			String errorMessage = "이미 매칭된 견적서 입니다.";

			Long estimateId = 300L;
			String changeEstimateStatus = EstimateStatus.OPEN.name();
			UpdateEstimateStatusRequest requestDto = new UpdateEstimateStatusRequest(changeEstimateStatus);

			Mockito.doThrow(new CustomException(HttpStatus.BAD_REQUEST, errorMessage))
				.when(estimateService).changeStatus(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI + "/{estimateId}/status", estimateId)
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
							.requestSchema(Schema.schema(UpdateEstimateStatusRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 견적서_상태_변경_기능_4XX_수용할_수_없는_견적서_상태인_경우() throws Exception {
			//given
			String errorMessage = "올바른 견적서 상태가 아닙니다.";

			Long estimateId = 300L;
			String changeEstimateStatus = "wrongStatus";
			UpdateEstimateStatusRequest requestDto = new UpdateEstimateStatusRequest(changeEstimateStatus);

			Mockito.doThrow(new CustomException(HttpStatus.BAD_REQUEST, errorMessage))
				.when(estimateService).changeStatus(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI + "/{estimateId}/status", estimateId)
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
							.requestSchema(Schema.schema(UpdateEstimateStatusRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}
	}
	
	@Nested
	@DisplayName("견적서 조건 검색 API 테스트")
	class SearchEstimates {
		@Test
		void 견적서_조건_검색_기능_2XX() throws Exception {
			//given
			SearchEstimateRequest searchEstimateRequest = new SearchEstimateRequest(
				null, null, null, null, null, null, null, null
			);
			PagingRequest pagingRequest = new PagingRequest(null, null, null);

			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			ReflectionTestUtils.setField(estimateFixture, "id", 404L);
			ReflectionTestUtils.setField(estimateFixture, "createDate", LocalDateTime.now());

			Estimate estimateFixture2 = EstimateFixture.ESTIMATE_FIXTURE_4.create();
			ReflectionTestUtils.setField(estimateFixture2, "id", 500L);
			ReflectionTestUtils.setField(estimateFixture2, "createDate", LocalDateTime.now());


			PagingResponse<EstimateDetailResponse> responseDto = PagingResponse.from(
				new PageImpl<>(List.of(estimateFixture, estimateFixture2)).map(EstimateDetailResponse::new));

			Mockito.when(estimateService.searchEstimates(searchEstimateRequest, pagingRequest))
				.thenReturn(responseDto);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI + "/search")
			);

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))

				// paging meta
				.andExpect(jsonPath("$.data.page.number").value(1))

				// content[0] = 견적서 정보
				.andExpect(jsonPath("$.data.content[0].estimateId").value(responseDto.content().get(0).estimateId()))
				.andExpect(jsonPath("$.data.content[0].repairCost").value(responseDto.content().get(0).repairCost()))
				.andExpect(jsonPath("$.data.content[0].repairParts").isArray())
				.andExpect(
					jsonPath("$.data.content[0].estimateStatus").value(responseDto.content().get(0).estimateStatus()))
				.andExpect(jsonPath("$.data.content[0].imagePath").value(responseDto.content().get(0).imagePath()))

				// 사고 레포트 정보
				.andExpect(jsonPath("$.data.content[0].damageReportInfo.description")
					.value(responseDto.content().get(0).damageReportInfo().description()))
				.andExpect(jsonPath("$.data.content[0].damageReportInfo.preferredRepairSido")
					.value(responseDto.content().get(0).damageReportInfo().preferredRepairSido()))
				.andExpect(jsonPath("$.data.content[0].damageReportInfo.preferredRepairSigungu")
					.value(responseDto.content().get(0).damageReportInfo().preferredRepairSigungu()))
				.andExpect(jsonPath("$.data.content[0].damageReportInfo.isPickupRequired")
					.value(responseDto.content().get(0).damageReportInfo().isPickupRequired()))

				// 차량 정보
				.andExpect(jsonPath("$.data.content[0].vehicleInfo.brand")
					.value(responseDto.content().get(0).vehicleInfo().brand()))
				.andExpect(jsonPath("$.data.content[0].vehicleInfo.licenseNumber")
					.value(responseDto.content().get(0).vehicleInfo().licenseNumber()))
				.andExpect(jsonPath("$.data.content[0].vehicleInfo.modelName")
					.value(responseDto.content().get(0).vehicleInfo().modelName()))
				.andExpect(jsonPath("$.data.content[0].vehicleInfo.modelYear")
					.value(responseDto.content().get(0).vehicleInfo().modelYear()))
				.andExpect(jsonPath("$.data.content[0].vehicleInfo.ownerName")
					.value(responseDto.content().get(0).vehicleInfo().ownerName()))
				.andExpect(jsonPath("$.data.content[0].vehicleInfo.ownershipType")
					.value(responseDto.content().get(0).vehicleInfo().ownershipType()))
				.andDo(restDocsHandler.document(
					ResourceDocumentation.resource(ResourceSnippetParameters.builder()
						.tag(BASE_TAG)
						.summary("견적서 조건 검색")
						.description("## 견적서 조건 검색 기능 \n"
							+ "### 설명 \n"
							+ "- 원하는 조건을 쿼리파라미터에 추가해주세요 (ex: ?brand=기아)"
						)
						.queryParameters(
							parameterWithName("size").description(
								"페이지에 표시할 size입니다. 10 ~ 100입니다. 만약 다른 값이 들어오면 10개로 고정합니다.").optional(),
							parameterWithName("page").description("page가 없거나, 음수라면 첫 페이지로 고정합니다.").optional(),
							parameterWithName("minRepairCost").description("최소 산정 금액").optional(),
							parameterWithName("maxRepairCost").description("최대 산정 금액").optional(),
							parameterWithName("sido").description("수리 희망 시/도").optional(),
							parameterWithName("sigungu").description("수리 희망 시/군/구").optional(),
							parameterWithName("isPickupRequired").description("수리 시 픽업 희망 유무").optional(),
							parameterWithName("brand").description("사고 차량의 브랜드").optional(),
							parameterWithName("modelYear").description("사고 차량의 연식").optional(),
							parameterWithName("modelName").description("사고 차량의 이름").optional()
						)
						.responseSchema(Schema.schema(PagingResponse.class.getSimpleName()))
						.responseFields(
							fieldWithPath("message").description("성공 응답 메세지입니다.").type(JsonFieldType.STRING),

							// paging wrapper
							fieldWithPath("data.content").description("페이징된 견적서 목록입니다.").type(JsonFieldType.ARRAY),
							fieldWithPath("data.page").description("페이지 메타데이터입니다.").type(JsonFieldType.OBJECT),

							// page meta
							fieldWithPath("data.page.number").description("현재 페이지 번호(1부터 시작)입니다.")
								.type(JsonFieldType.NUMBER),
							fieldWithPath("data.page.size").description("페이지 크기입니다.").type(JsonFieldType.NUMBER),
							fieldWithPath("data.page.totalElements").description("전체 요소 개수입니다.")
								.type(JsonFieldType.NUMBER),
							fieldWithPath("data.page.totalPages").description("전체 페이지 수입니다.")
								.type(JsonFieldType.NUMBER),
							fieldWithPath("data.page.hasNext").description("다음 페이지 존재 여부입니다.")
								.type(JsonFieldType.BOOLEAN),
							fieldWithPath("data.page.hasPrevious").description("이전 페이지 존재 여부입니다.")
								.type(JsonFieldType.BOOLEAN),

							// content
							fieldWithPath("data.content[].estimateId").description("견적서 식별자입니다.")
								.type(JsonFieldType.NUMBER),
							fieldWithPath("data.content[].repairCost").description("사고 견적 분석 금액입니다.")
								.type(JsonFieldType.NUMBER),
							fieldWithPath("data.content[].repairParts").description("사고 수리 예상 부위입니다.")
								.type(JsonFieldType.ARRAY),
							fieldWithPath("data.content[].estimateStatus").description("견적서의 상태입니다.")
								.type(JsonFieldType.STRING),
							fieldWithPath("data.content[].imagePath").description("견적서의 사고 분석 사진 주소입니다.")
								.type(JsonFieldType.STRING),
							fieldWithPath("data.content[].createdAt").description("견적서 생성일 입니다.")
								.type(JsonFieldType.STRING),

							// 사고 레포트 정보
							fieldWithPath("data.content[].damageReportInfo").description("사고 레포트 정보입니다.")
								.type(JsonFieldType.OBJECT),
							fieldWithPath("data.content[].damageReportInfo.description").description(
								"사용자가 기술한 사고 레포트의 내용입니다.").type(JsonFieldType.STRING),
							fieldWithPath("data.content[].damageReportInfo.preferredRepairSido").description(
								"수리 희망 시/도 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("data.content[].damageReportInfo.preferredRepairSigungu").description(
								"수리 희망 시/군/구 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("data.content[].damageReportInfo.isPickupRequired").description(
								"사고 수리 픽업 희망 유무입니다.").type(JsonFieldType.BOOLEAN),

							// 차량 정보
							fieldWithPath("data.content[].vehicleInfo").description("사고 차량 정보입니다.")
								.type(JsonFieldType.OBJECT),
							fieldWithPath("data.content[].vehicleInfo.brand").description("사고 차량의 브랜드입니다.")
								.type(JsonFieldType.STRING),
							fieldWithPath("data.content[].vehicleInfo.licenseNumber").description("차량의 번호입니다.")
								.type(JsonFieldType.STRING),
							fieldWithPath("data.content[].vehicleInfo.modelName").description("사고 차량의 모델명입니다.")
								.type(JsonFieldType.STRING),
							fieldWithPath("data.content[].vehicleInfo.modelYear").description("사고 차량의 연식입니다.")
								.type(JsonFieldType.NUMBER),
							fieldWithPath("data.content[].vehicleInfo.ownerName").description("사고 차량의 실소유자명입니다.")
								.type(JsonFieldType.STRING),
							fieldWithPath("data.content[].vehicleInfo.ownershipType").description("차량의 유형입니다.")
								.type(JsonFieldType.STRING)
						)
						.build()
					)
				));
		}
	}
}