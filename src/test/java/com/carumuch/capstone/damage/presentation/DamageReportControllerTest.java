package com.carumuch.capstone.damage.presentation;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.ApiErrorResponse;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.presentation.dto.request.report.RegisterDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.request.report.UpdateDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.response.report.DamageReportInfoResponse;
import com.carumuch.capstone.support.RestDocsSupport;
import com.carumuch.capstone.support.fixture.DamageReportFixture;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;

class DamageReportControllerTest extends RestDocsSupport {

	private static final String BASE_URI = "/damage-reports";
	private static final String BASE_TAG = "Damage - Report";

	@Nested
	@DisplayName("사고 레포트 등록 API 테스트")
	class Register {
		@Test
		void 사고_레포트_등록_2XX() throws Exception {
		    //given
			Mockito.when(damageReportService.register(Mockito.any(RegisterDamageReportRequest.class), Mockito.anyLong()))
				.thenReturn(1L);

			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			RegisterDamageReportRequest requestDto = new RegisterDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath()
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
						.summary("사고 레포트 등록")
						.description("## 사고 레포트 등록 기능 \n"
							+ "### 사용법 \n"
							+ "- 필드의 validation을 확인해주세요.\n"
							+ "- 사고 레포트가 등록되면 AI 견적서 작업을 시작합니다."
						)
						.requestSchema(Schema.schema(RegisterDamageReportRequest.class.getSimpleName()))
						.requestFields(
							fieldWithPath("description").description("사고 레포트 내용입니다.").type(JsonFieldType.STRING),
							fieldWithPath("preferredRepairSido").description("사고 수리 희망 지역 시/도 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("preferredRepairSigungu").description("사고 수리 희망 지역 시/군/구 입니다.").type(JsonFieldType.STRING),
							fieldWithPath("isPickupRequired").description("사고 처리 시 차량 픽업 희망 여부 입니다.").type(JsonFieldType.BOOLEAN),
							fieldWithPath("imagePath").description("사고 부위 사진 URL입니다.").type(JsonFieldType.STRING)
						)
						.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
						.build())
					)
				);
		}

		@Test
		void 사고_레포트_등록_4XX_등록된_차량이_없는_경우() throws Exception {
		    //given
			String errorMessage = Vehicle.class.getSimpleName() + "을(를) 찾을 수 없습니다.";

			Mockito.doThrow(new NotFoundException(Vehicle.class))
				.when(damageReportService).register(Mockito.any(RegisterDamageReportRequest.class), Mockito.anyLong());

			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			RegisterDamageReportRequest requestDto = new RegisterDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath()
			);

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
						.requestSchema(Schema.schema(RegisterDamageReportRequest.class.getSimpleName()))
						.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
						.build())
					)
				);
		}
	}

	@Nested
	@DisplayName("사고 레포트 수정 API 테스트")
	class Update {
		@Test
		void 사고_레포트_수정_2XX() throws Exception {
			//given
			Long damageReportId = 1L;

			Mockito.doNothing()
				.when(damageReportService)
				.update(Mockito.any(UpdateDamageReportRequest.class), Mockito.anyLong(), Mockito.anyLong());

			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			UpdateDamageReportRequest requestDto = new UpdateDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired()
			);

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI+ "/{damageReportId}", damageReportId)
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
							.summary("사고 레포트 수정")
							.description("## 사고 레포트 수정 기능 \n"
								+ "### 사용법 \n"
								+ "- 필드의 validation을 확인해주세요.\n"
								+ "- 이미 처리 중인 사고 레포트는 등록된 이미지는 수정할 수 없습니다."
							)
							.requestSchema(Schema.schema(UpdateDamageReportRequest.class.getSimpleName()))
							.requestFields(
								fieldWithPath("description").description("사고 레포트 내용입니다.").type(JsonFieldType.STRING),
								fieldWithPath("preferredRepairSido").description("사고 수리 희망 지역 시/도 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("preferredRepairSigungu").description("사고 수리 희망 지역 시/군/구 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("isPickupRequired").description("사고 처리 시 차량 픽업 희망 여부 입니다.").type(JsonFieldType.BOOLEAN)
								)
							.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
							.build())
					)
				);
		}

		@Test
		void 사고_레포트_수정_4XX_등록된_사고_레포트가_없는_경우() throws Exception {
			//given
			String errorMessage = DamageReport.class.getSimpleName() + "을(를) 찾을 수 없습니다.";
			Long damageReportId = 1L;

			Mockito.doThrow(new NotFoundException(DamageReport.class))
				.when(damageReportService).update(Mockito.any(UpdateDamageReportRequest.class), Mockito.anyLong(), Mockito.anyLong());

			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			UpdateDamageReportRequest requestDto = new UpdateDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired()
			);

			//when
			ResultActions actions = mockMvc.perform(
				put(BASE_URI+ "/{damageReportId}", damageReportId)
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
							.requestSchema(Schema.schema(UpdateDamageReportRequest.class.getSimpleName()))
							.responseSchema(Schema.schema(ApiErrorResponse.class.getSimpleName()))
							.build())
					)
				);
		}
	}

	@Nested
	@DisplayName("최근 등록한 사고 레포트 조회 API 테스트")
	class FindRecentReports {
		@Test
		void 최근_등록된_사고_레포트_목록_조회_2XX() throws Exception {
			//given
			DamageReport damageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			DamageReportInfoResponse damageReportInfoResponse = new DamageReportInfoResponse(
				1L,
				damageReport.getDescription(),
				damageReport.getPreferredRepairRegion().getSido(),
				damageReport.getPreferredRepairRegion().getSigungu(),
				damageReport.isPickupRequired(),
				damageReport.getStatus().name(),
				LocalDateTime.now()
			);

			Mockito.when(damageReportService.findRecentReports(Mockito.anyLong()))
				.thenReturn(IntStream.range(0, 10)
					.mapToObj(i -> damageReportInfoResponse)
					.toList()
				);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI + "/recent")
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.summary("최근 등록한 사고 레포트 목록 조회")
							.description("## 최근 등록한 사고 레포트 목록 조회 기능 \n"
								+ "### 사용법 \n"
								+ "- 최근 등록한 10개의 사고 레포트를 조회합니다.\n"
								+ "- 그 이상 조회 필요시 전체 목록 조회로 사고 레포트를 확인해야합니다."
							)
							.responseSchema(Schema.schema(DamageReportInfoResponse.class.getSimpleName()))
							.responseFields(
								fieldWithPath("message").description("성공 응답 메세지입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data[].id").description("사고 레포트 PK입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data[].description").description("사고 레포트 내용입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data[].preferredRepairSido").description("사고 수리 희망 지역 시/도 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data[].preferredRepairSigungu").description("사고 수리 희망 지역 시/군/구 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data[].isPickupRequired").description("사고 수리 차량 픽업 희망 여부입니다.").type(JsonFieldType.BOOLEAN),
								fieldWithPath("data[].status").description("사고 레포트의 처리 상태입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data[].createdAt").description("사고 레포트가 등록된 시간입니다.").type(JsonFieldType.STRING)
							)
							.build())
					)
				);
		}
	}

	@Nested
	@DisplayName("사고 레포트 전체 목록 조회 API 테스트")
	class FindReports {
		@Test
		void 사고_레포트_전체_목록_조회_2XX_첫_페이지() throws Exception {
			//given
			int number = 0;
			int size = 10;

			DamageReport damageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			DamageReportInfoResponse damageReportInfoResponse = new DamageReportInfoResponse(
				1L,
				damageReport.getDescription(),
				damageReport.getPreferredRepairRegion().getSido(),
				damageReport.getPreferredRepairRegion().getSigungu(),
				damageReport.isPickupRequired(),
				damageReport.getStatus().name(),
				LocalDateTime.now()
			);
			List<DamageReportInfoResponse> damageReportInfoResponses = IntStream.range(0, size)
				.mapToObj(i -> damageReportInfoResponse)
				.toList();

			PagingResponse<DamageReportInfoResponse> pagingResponse = PagingResponse.from(
				new PageImpl<>(damageReportInfoResponses, PageRequest.of(number, size), 30));

			Mockito.when(damageReportService.findReports(Mockito.anyLong(), Mockito.any(PagingRequest.class)))
				.thenReturn(pagingResponse);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI)
					.param("page", String.valueOf(1))
					.param("size", String.valueOf(size))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.summary("사고 레포트 전체 목록 조회")
							.description("## 사고 레포트 목록 조회 기능 \n"
								+ "### 사용법 \n"
								+ "- size는 10 ~ 100입니다. 만약 다른 값이 들어오면 10개로 고정합니다.\n"
								+ "- page가 없거나, 음수라면 첫 페이지로 고정합니다."
							)
							.responseSchema(Schema.schema(PagingResponse.class.getSimpleName()))
							.responseFields(
								fieldWithPath("message").description("성공 응답 메세지입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.page.number").description("현재 조회 페이지 번호입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.page.size").description("한 페이지의 데이터 개수입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.page.totalElements").description("조회할 수 있는 전체 데이터의 개수입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.page.totalPages").description("조회할 수 있는 전체 페이지 번호입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.page.hasNext").description("다음 페이지의 여부입니다.").type(JsonFieldType.BOOLEAN),
								fieldWithPath("data.page.hasPrevious").description("이전 페이지 여부입니다.").type(JsonFieldType.BOOLEAN),
								fieldWithPath("data.content[].id").description("사고 레포트 PK입니다.").type(JsonFieldType.NUMBER),
								fieldWithPath("data.content[].description").description("사고 레포트 내용입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.content[].preferredRepairSido").description("사고 수리 희망 지역 시/도 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.content[].preferredRepairSigungu").description("사고 수리 희망 지역 시/군/구 입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.content[].isPickupRequired").description("사고 수리 차량 픽업 희망 여부입니다.").type(JsonFieldType.BOOLEAN),
								fieldWithPath("data.content[].status").description("사고 레포트의 처리 상태입니다.").type(JsonFieldType.STRING),
								fieldWithPath("data.content[].createdAt").description("사고 레포트가 등록된 시간입니다.").type(JsonFieldType.STRING)
							)
							.build())
					)
				);
		}

		@Test
		void 사고_레포트_전체_목록_조회_2XX_두번째_페이지() throws Exception {
			//given
			int number = 1;
			int size = 10;

			DamageReport damageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			DamageReportInfoResponse damageReportInfoResponse = new DamageReportInfoResponse(
				1L,
				damageReport.getDescription(),
				damageReport.getPreferredRepairRegion().getSido(),
				damageReport.getPreferredRepairRegion().getSigungu(),
				damageReport.isPickupRequired(),
				damageReport.getStatus().name(),
				LocalDateTime.now()
			);
			List<DamageReportInfoResponse> damageReportInfoResponses = IntStream.range(0, size)
				.mapToObj(i -> damageReportInfoResponse)
				.toList();

			PagingResponse<DamageReportInfoResponse> pagingResponse = PagingResponse.from(
				new PageImpl<>(damageReportInfoResponses, PageRequest.of(number, size), 30));

			Mockito.when(damageReportService.findReports(Mockito.anyLong(), Mockito.any(PagingRequest.class)))
				.thenReturn(pagingResponse);

			//when
			ResultActions actions = mockMvc.perform(
				get(BASE_URI)
					.param("page", String.valueOf(2))
					.param("size", String.valueOf(size))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andDo(restDocsHandler.document(
						ResourceDocumentation.resource(ResourceSnippetParameters.builder()
							.tag(BASE_TAG)
							.responseSchema(Schema.schema(PagingResponse.class.getSimpleName()))
							.build())
					)
				);
		}
	}
}