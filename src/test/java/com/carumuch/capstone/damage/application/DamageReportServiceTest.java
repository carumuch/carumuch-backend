package com.carumuch.capstone.damage.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;
import com.carumuch.capstone.damage.domain.vehicle.VehicleRepository;
import com.carumuch.capstone.damage.presentation.dto.request.report.RegisterDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.request.report.UpdateDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.response.report.DamageReportInfoResponse;
import com.carumuch.capstone.support.fixture.DamageReportFixture;

@ExtendWith(MockitoExtension.class)
class DamageReportServiceTest {

	@InjectMocks
	DamageReportService damageReportService;
	@Mock
	DamageReportRepository damageReportRepository;
	@Mock
	VehicleRepository vehicleRepository;

	@Nested
	@DisplayName("사고 레포트 등록")
	class Register {
		@Test
		void 등록된_사용자의_차량을_조회한다() {
		    //given
			Long vehicleId = 1L;
			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			RegisterDamageReportRequest registerDamageReportRequest = new RegisterDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath()
			);
			Mockito.when(vehicleRepository.findByUserId(vehicleId)).thenReturn(Optional.of(damageReportFixture.getVehicle()));
			DamageReport damageReport = registerDamageReportRequest.toEntity(damageReportFixture.getVehicle());
			ReflectionTestUtils.setField(damageReport, "id", 1L);
			Mockito.when(damageReportRepository.save(Mockito.any(DamageReport.class))).thenReturn(damageReport);

		    //when
			damageReportService.register(registerDamageReportRequest, vehicleId);

		    //then
		    Mockito.verify(vehicleRepository, Mockito.times(1))
				.findByUserId(vehicleId);
		}

		@Test
		void 등록된_사용자의_차량이_없으면_예외를_반환한다() {
		    //given
			Long vehicleId = 1L;
			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			RegisterDamageReportRequest registerDamageReportRequest = new RegisterDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath()
			);
			Mockito.when(vehicleRepository.findByUserId(vehicleId)).thenReturn(Optional.empty());

		    //when & then
			Assertions.assertThatThrownBy(() -> damageReportService.register(registerDamageReportRequest, vehicleId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 사고_레포트를_등록한다() {
		    //given
			Long vehicleId = 1L;
			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			RegisterDamageReportRequest registerDamageReportRequest = new RegisterDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath()
			);
			Mockito.when(vehicleRepository.findByUserId(vehicleId)).thenReturn(Optional.of(damageReportFixture.getVehicle()));

			DamageReport damageReport = registerDamageReportRequest.toEntity(damageReportFixture.getVehicle());
			ReflectionTestUtils.setField(damageReport, "id", 1L);
			Mockito.when(damageReportRepository.save(Mockito.any(DamageReport.class))).thenReturn(damageReport);

		    //when
			Long result = damageReportService.register(registerDamageReportRequest, vehicleId);

			//then
			Mockito.verify(damageReportRepository, Mockito.times(1))
				.save(Mockito.any(DamageReport.class));
			Assertions.assertThat(result).isInstanceOf(Long.class);
		}
	}

	@Nested
	@DisplayName("사고 레포트 수정")
	class Update {
		@Test
		void 사고_레포트를_조회한다() {
		    //given
			Long userId = 1L;
			Long damageReportId = 1L;
			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

			UpdateDamageReportRequest updateDamageReportRequest = new UpdateDamageReportRequest(
				"설명을 변경합니다.",
				"부산시",
				"남구",
				true
			);

			Mockito.when(damageReportRepository.findByIdAndUserId(damageReportId, userId)).thenReturn(Optional.of(damageReportFixture));

		    //when
			damageReportService.update(updateDamageReportRequest, damageReportId, userId);

		    //then
		    Mockito.verify(damageReportRepository, Mockito.times(1))
				.findByIdAndUserId(damageReportId, userId);
		}

		@Test
		void 사고_레포트가_존재하지_않는다면_예외를_반환한다() {
		    //given
			Long userId = 1L;
			Long damageReportId = 1L;

			UpdateDamageReportRequest updateDamageReportRequest = new UpdateDamageReportRequest(
				"설명을 변경합니다.",
				"부산시",
				"남구",
				true
			);

			Mockito.when(damageReportRepository.findByIdAndUserId(damageReportId, userId)).thenReturn(Optional.empty());

		    //when & then
			Assertions.assertThatThrownBy(() -> damageReportService.update(updateDamageReportRequest, damageReportId, userId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 사고_레포트를_업데이트한다() {
		    //given
			Long userId = 1L;
			Long damageReportId = 1L;
			DamageReport damageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

			String changeDescription = "설명을 변경합니다.";
			String changeSido = "부산시";
			String changeSigungu = "남구";
			boolean changeIsPickupRequired = true;

			UpdateDamageReportRequest updateDamageReportRequest = new UpdateDamageReportRequest(
				changeDescription,
				changeSido,
				changeSigungu,
				changeIsPickupRequired
			);

			Mockito.when(damageReportRepository.findByIdAndUserId(damageReportId, userId)).thenReturn(Optional.of(damageReport));

		    //when
			damageReportService.update(updateDamageReportRequest, damageReportId, userId);

		    //then
			assertAll(
				() -> Assertions.assertThat(damageReport.getDescription()).isEqualTo(changeDescription),
				() -> Assertions.assertThat(damageReport.getPreferredRepairRegion().getSido()).isEqualTo(changeSido),
				() -> Assertions.assertThat(damageReport.getPreferredRepairRegion().getSigungu()).isEqualTo(changeSigungu),
				() -> Assertions.assertThat(damageReport.isPickupRequired()).isEqualTo(changeIsPickupRequired)
			);
		}
	}

	@Nested
	@DisplayName("최근 사고 레포트 기록 조회")
	class FindRecentReports {

		DamageReport damageReport1 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
		DamageReport damageReport2 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
		DamageReport damageReport3 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_3.create();
		DamageReport damageReport4 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
		DamageReport damageReport5 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
		DamageReport damageReport6 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_3.create();
		DamageReport damageReport7 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
		DamageReport damageReport8 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
		DamageReport damageReport9 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_3.create();
		DamageReport damageReport10 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

		List<DamageReport> damageReports = List.of(
			damageReport1,
			damageReport2,
			damageReport3,
			damageReport4,
			damageReport5,
			damageReport6,
			damageReport7,
			damageReport8,
			damageReport9,
			damageReport10
		);

		@Test
		void 최근_사고_레포트_10개_목록을_조회한다() {
		    //given
		    Long userId = 1L;
			Mockito.when(damageReportRepository.findRecent10ByUserId(userId)).thenReturn(damageReports);

		    //when
			damageReportService.findRecentReports(userId);

		    //then
		    Mockito.verify(damageReportRepository, Mockito.times(1))
				.findRecent10ByUserId(userId);
		}

		@Test
		void 최근_사고_레포트_10개_목록을_응답한다() {
		    //given
			Long userId = 1L;
			Mockito.when(damageReportRepository.findRecent10ByUserId(userId)).thenReturn(damageReports);

		    //when
			List<DamageReportInfoResponse> results = damageReportService.findRecentReports(userId);

			//then
		    assertAll(
				() -> Assertions.assertThat(results).hasSize(10),
				() -> Assertions.assertThat(results.get(0).preferredRepairSido()).isEqualTo(damageReport1.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(1).preferredRepairSido()).isEqualTo(damageReport2.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(2).preferredRepairSido()).isEqualTo(damageReport3.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(3).preferredRepairSido()).isEqualTo(damageReport4.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(4).preferredRepairSido()).isEqualTo(damageReport5.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(5).preferredRepairSido()).isEqualTo(damageReport6.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(6).preferredRepairSido()).isEqualTo(damageReport7.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(7).preferredRepairSido()).isEqualTo(damageReport8.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(8).preferredRepairSido()).isEqualTo(damageReport9.getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(9).preferredRepairSido()).isEqualTo(damageReport10.getPreferredRepairRegion().getSido())
			);
		}
	}
	
	@Nested
	@DisplayName("사고 레포트 페이지 조회 기능")
	class FindReports {
		DamageReport damageReport1 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
		DamageReport damageReport2 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
		DamageReport damageReport3 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_3.create();
		DamageReport damageReport4 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
		DamageReport damageReport5 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
		DamageReport damageReport6 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_3.create();
		DamageReport damageReport7 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
		DamageReport damageReport8 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
		DamageReport damageReport9 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_3.create();
		DamageReport damageReport10 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

		List<DamageReport> damageReportsFirstPage = List.of(
			damageReport1,
			damageReport2,
			damageReport3,
			damageReport4,
			damageReport5,
			damageReport6,
			damageReport7,
			damageReport8,
			damageReport9,
			damageReport10
		);

		DamageReport damageReport11 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
		DamageReport damageReport12 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_3.create();
		DamageReport damageReport13 = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

		List<DamageReport> damageReportsSecondPage = List.of(
			damageReport11,
			damageReport12,
			damageReport13
		);

		@Test
		void 사고_레포트_페이지를_조회한다() {
		    //given
			Long userId = 1L;
			PagingRequest pagingRequest = new PagingRequest(1, 10, null);
			Page<DamageReport> page = new PageImpl<>(
				damageReportsFirstPage,
				PageRequest.of(pagingRequest.page(), pagingRequest.size()),
				13
			);

			Mockito.when(damageReportRepository.findPageByUserId(userId, pagingRequest.page(), pagingRequest.size(), pagingRequest.sort()))
				.thenReturn(page);
		    
		    //when
			damageReportService.findReports(userId, pagingRequest);
		    
		    //then
		    Mockito.verify(damageReportRepository, Mockito.times(1))
				.findPageByUserId(userId, pagingRequest.page(), pagingRequest.size(), pagingRequest.sort());
		}
		
		@Test
		void 사고_레포트_첫번째_페이지를_조회한다() {
		    //given
			Long userId = 1L;
			PagingRequest pagingRequest = new PagingRequest(1, 10, null);
			Page<DamageReport> page = new PageImpl<>(
				damageReportsFirstPage,
				PageRequest.of(pagingRequest.page(), pagingRequest.size()),
				13
			);

			Mockito.when(damageReportRepository.findPageByUserId(userId, pagingRequest.page(), pagingRequest.size(), pagingRequest.sort()))
				.thenReturn(page);
		    
		    //when
			PagingResponse<DamageReportInfoResponse> results = damageReportService.findReports(userId, pagingRequest);

			//then
		    assertAll(
				() -> Assertions.assertThat(results.content()).hasSize(10),
				() -> Assertions.assertThat(results.page().totalElements()).isEqualTo(13),
				() -> Assertions.assertThat(results.page().totalPages()).isEqualTo(2),
				() -> Assertions.assertThat(results.page().hasNext()).isTrue(),
				() -> Assertions.assertThat(results.page().hasPrevious()).isFalse(),
				() -> Assertions.assertThat(results.page().number()).isEqualTo(1)
			);
		}

		@Test
		void 사고_레포트_두번째_페이지를_조회한다() {
			//given
			Long userId = 1L;
			PagingRequest pagingRequest = new PagingRequest(2, 10, null);
			Page<DamageReport> page = new PageImpl<>(
				damageReportsSecondPage,
				PageRequest.of(pagingRequest.page(), pagingRequest.size()),
				13
			);

			Mockito.when(damageReportRepository.findPageByUserId(userId, pagingRequest.page(), pagingRequest.size(), pagingRequest.sort()))
				.thenReturn(page);

			//when
			PagingResponse<DamageReportInfoResponse> results = damageReportService.findReports(userId, pagingRequest);

			//then
			assertAll(
				() -> Assertions.assertThat(results.content()).hasSize(3),
				() -> Assertions.assertThat(results.page().totalElements()).isEqualTo(13),
				() -> Assertions.assertThat(results.page().totalPages()).isEqualTo(2),
				() -> Assertions.assertThat(results.page().hasNext()).isFalse(),
				() -> Assertions.assertThat(results.page().hasPrevious()).isTrue(),
				() -> Assertions.assertThat(results.page().number()).isEqualTo(2)
			);
		}
	}
}