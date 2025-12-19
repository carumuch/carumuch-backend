package com.carumuch.capstone.damage.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.damage.application.DamageReportService;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.domain.vehicle.VehicleRepository;
import com.carumuch.capstone.damage.presentation.dto.request.report.RegisterDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.request.report.UpdateDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.response.report.DamageReportInfoResponse;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;
import com.carumuch.capstone.support.IntegrationSupportTest;
import com.carumuch.capstone.support.fixture.DamageReportFixture;
import com.carumuch.capstone.support.fixture.UserFixture;
import com.carumuch.capstone.support.fixture.VehicleFixture;

public class DamageReportIntegrationTest extends IntegrationSupportTest {

	@Autowired
	DamageReportService damageReportService;
	@Autowired
	VehicleRepository vehicleRepository;
	@Autowired
	DamageReportRepository damageReportRepository;
	@Autowired
	UserRepository userRepository;

	User user;
	Vehicle vehicle;

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserFixture.USER_FIXTURE_1.create());
		Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
		vehicle = vehicleRepository.save(
			new Vehicle(
				vehicleFixture.getLicenseNumber().getValue(),
				vehicleFixture.getOwnershipType(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName(),
				user
			)
		);
	}

	@Nested
	@DisplayName("사고 레포트 등록 기능")
	class Register {
		@Test
		void 사고_레포트를_등록한다() {
		    //given
			Long userId = user.getId();
			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

			RegisterDamageReportRequest registerDamageReportRequest = new RegisterDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath()
			);

		    //when
			Long result = damageReportService.register(registerDamageReportRequest, userId);

			//then
		    Assertions.assertThat(result).isInstanceOf(Long.class);
		}

		@Test
		void 등록된_차량이_없다면_예외를_반환한다() {
		    //given
			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

			RegisterDamageReportRequest registerDamageReportRequest = new RegisterDamageReportRequest(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion().getSido(),
				damageReportFixture.getPreferredRepairRegion().getSigungu(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath()
			);

			User otherUser = userRepository.save(UserFixture.USER_FIXTURE_2.create());

		    //when & then
			Assertions.assertThatThrownBy(() -> damageReportService.register(registerDamageReportRequest, otherUser.getId()))
				.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	@DisplayName("사고 레포트 수정 기능")
	class Update {
		@Test
		void 사고_레포트를_수정한다() {
		    //given
			DamageReport damageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			Long damageReportId = damageReportRepository.save(
				new DamageReport(
					damageReport.getDescription(),
					damageReport.getPreferredRepairRegion(),
					damageReport.isPickupRequired(),
					damageReport.getImagePath(),
					vehicle
				)
			).getId();

			DamageReport updatedDamageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
			UpdateDamageReportRequest updateDamageReportRequest = new UpdateDamageReportRequest(
				updatedDamageReport.getDescription(),
				updatedDamageReport.getPreferredRepairRegion().getSido(),
				updatedDamageReport.getPreferredRepairRegion().getSigungu(),
				updatedDamageReport.isPickupRequired()
			);

		    //when
			damageReportService.update(updateDamageReportRequest, damageReportId, user.getId());

		    //then
			DamageReport result = damageReportRepository.findById(damageReportId)
				.orElseThrow(() -> new AssertionError("damageReport not found"));
			assertAll(
				() -> Assertions.assertThat(result.getDescription()).isEqualTo(updateDamageReportRequest.description()),
				() -> Assertions.assertThat(result.getPreferredRepairRegion().getSido()).isEqualTo(updateDamageReportRequest.preferredRepairSido()),
				() -> Assertions.assertThat(result.getPreferredRepairRegion().getSigungu()).isEqualTo(updateDamageReportRequest.preferredRepairSigungu()),
				() -> Assertions.assertThat(result.isPickupRequired()).isEqualTo(updateDamageReportRequest.isPickupRequired())
			);
		}

		@Test
		void 사고_레포트를_찾을_수_없다면_예외를_반환한다() {
		    //given
			Long noSavedDamageReportId = 1L;
			DamageReport updatedDamageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_2.create();
			UpdateDamageReportRequest updateDamageReportRequest = new UpdateDamageReportRequest(
				updatedDamageReport.getDescription(),
				updatedDamageReport.getPreferredRepairRegion().getSido(),
				updatedDamageReport.getPreferredRepairRegion().getSigungu(),
				updatedDamageReport.isPickupRequired()
			);

		    //when & then
			Assertions.assertThatThrownBy(
				() -> damageReportService.update(updateDamageReportRequest, noSavedDamageReportId, user.getId()))
				.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	@DisplayName("최근 사고 레포트 목록 조회 기능")
	class FindRecentReports {
		@Test
		void 최근_사고_레포트_목록_10개를_조회한다() {
		    //given
			DamageReport damageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			List<DamageReport> damageReports = IntStream.range(0, 15)
				.mapToObj(i -> damageReportRepository.save(
					new DamageReport(
						damageReport.getDescription(),
						damageReport.getPreferredRepairRegion(),
						damageReport.isPickupRequired(),
						damageReport.getImagePath(),
						vehicle
					))
				).toList();

		    //when
			List<DamageReportInfoResponse> results = damageReportService.findRecentReports(user.getId());

			//then
			assertAll(
				() -> Assertions.assertThat(results.size()).isEqualTo(10),
				() -> Assertions.assertThat(results.get(0).description()).isEqualTo(damageReports.get(0).getDescription()),
				() -> Assertions.assertThat(results.get(0).preferredRepairSigungu()).isEqualTo(damageReports.get(0).getPreferredRepairRegion().getSigungu()),
				() -> Assertions.assertThat(results.get(0).preferredRepairSido()).isEqualTo(damageReports.get(0).getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(0).isPickupRequired()).isEqualTo(damageReports.get(0).isPickupRequired()),
				() -> Assertions.assertThat(results.get(9).preferredRepairSigungu()).isEqualTo(damageReports.get(9).getPreferredRepairRegion().getSigungu()),
				() -> Assertions.assertThat(results.get(9).preferredRepairSido()).isEqualTo(damageReports.get(9).getPreferredRepairRegion().getSido()),
				() -> Assertions.assertThat(results.get(9).isPickupRequired()).isEqualTo(damageReports.get(9).isPickupRequired())
			);
		}
	}

	@Nested
	@DisplayName("사고 레포트 전체 목록 조회 기능")
	class FindReports {
		@Test
		void 사고_레포트_페이지를_조회한다() {
		    //given
			Integer page = 1;
			Integer size = 10;
			int totalElements = 15;

			DamageReport damageReport = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
			List<DamageReport> damageReports = IntStream.range(0, totalElements)
				.mapToObj(i -> damageReportRepository.save(
					new DamageReport(
						damageReport.getDescription(),
						damageReport.getPreferredRepairRegion(),
						damageReport.isPickupRequired(),
						damageReport.getImagePath(),
						vehicle
					))
				).toList();

		    //when
			PagingResponse<DamageReportInfoResponse> results = damageReportService.findReports(
				user.getId(),
				new PagingRequest(page, size, null)
			);

			//then
			assertAll(
				() -> Assertions.assertThat(results.page().number()).isEqualTo(page),
				() -> Assertions.assertThat(results.page().size()).isEqualTo(size),
				() -> Assertions.assertThat(results.page().totalPages()).isEqualTo(2),
				() -> Assertions.assertThat(results.page().totalElements()).isEqualTo(totalElements),
				() -> Assertions.assertThat(results.page().hasNext()).isTrue(),
				() -> Assertions.assertThat(results.page().hasPrevious()).isFalse(),
				() -> Assertions.assertThat(results.content().size()).isEqualTo(size)
			);
		}
	}
}
