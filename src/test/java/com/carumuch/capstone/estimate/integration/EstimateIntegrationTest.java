package com.carumuch.capstone.estimate.integration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.domain.vehicle.VehicleRepository;
import com.carumuch.capstone.estimate.application.EstimateService;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResponse;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;
import com.carumuch.capstone.support.IntegrationSupportTest;
import com.carumuch.capstone.support.fixture.DamageReportFixture;
import com.carumuch.capstone.support.fixture.EstimateFixture;
import com.carumuch.capstone.support.fixture.UserFixture;
import com.carumuch.capstone.support.fixture.VehicleFixture;

public class EstimateIntegrationTest extends IntegrationSupportTest {

	@Autowired
	EstimateService estimateService;

	@Autowired
	EstimateRepository estimateRepository;

	@Autowired
	DamageReportRepository damageReportRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	@Autowired
	UserRepository userRepository;

	Estimate estimate;
	DamageReport damageReport;
	Vehicle vehicle;

	@BeforeEach
	void setUp() {
		User userFixture = UserFixture.USER_FIXTURE_1.create();
		User user = userRepository.save(
			new User(
				userFixture.getLoginId(),
				userFixture.getPassword(),
				userFixture.getEmail(),
				userFixture.getName(),
				userFixture.getRole()
			)
		);

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

		DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();
		damageReport = damageReportRepository.save(
			new DamageReport(
				damageReportFixture.getDescription(),
				damageReportFixture.getPreferredRepairRegion(),
				damageReportFixture.isPickupRequired(),
				damageReportFixture.getImagePath(),
				vehicle
			)
		);

		Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
		estimate = estimateRepository.save(
			new Estimate(
				estimateFixture.getRepairCost(),
				estimateFixture.getRepairParts(),
				estimateFixture.getEstimateStatus(),
				estimateFixture.getImagePath(),
				damageReport
			)
		);
	}

	@Nested
	@DisplayName("견적서 상세 조회 기능")
	class FindEstimateDetail {
		@Test
		void 견적서_상세_정보를_조회한다() {
		    //given
			Long estimateId = estimate.getId();

		    //when
			EstimateDetailResponse estimateDetail = estimateService.findEstimateDetail(estimateId);

			//then
			Estimate result = estimateRepository.findDetailById(estimateId)
				.orElseThrow(() -> new AssertionError("estimate not found"));

			assertAll(
				() -> assertThat(estimateDetail.estimateId()).isEqualTo(result.getId()),
				() -> assertThat(estimateDetail.estimateStatus()).isEqualTo(result.getEstimateStatus().name()),
				() -> assertThat(estimateDetail.estimateId()).isEqualTo(result.getId()),
				() -> assertThat(estimateDetail.repairCost()).isEqualTo(result.getRepairCost()),
				() -> assertThat(estimateDetail.repairParts().size()).isEqualTo(result.getRepairParts().size())
			);
			assertAll(
				() -> assertThat(estimateDetail.damageReportInfo().isPickupRequired()).isEqualTo(result.getDamageReport().isPickupRequired()),
				() -> assertThat(estimateDetail.damageReportInfo().preferredRepairSido()).isEqualTo(result.getDamageReport().getPreferredRepairRegion().getSido()),
				() -> assertThat(estimateDetail.damageReportInfo().preferredRepairSigungu()).isEqualTo(result.getDamageReport().getPreferredRepairRegion().getSigungu()),
				() -> assertThat(estimateDetail.damageReportInfo().description()).isEqualTo(result.getDamageReport().getDescription())
			);
			assertAll(
				() -> assertThat(estimateDetail.vehicleInfo().brand()).isEqualTo(result.getDamageReport().getVehicle().getBrand()),
				() -> assertThat(estimateDetail.vehicleInfo().ownershipType()).isEqualTo(result.getDamageReport().getVehicle().getOwnershipType().name()),
				() -> assertThat(estimateDetail.vehicleInfo().ownerName()).isEqualTo(result.getDamageReport().getVehicle().getOwnerName()),
				() -> assertThat(estimateDetail.vehicleInfo().modelName()).isEqualTo(result.getDamageReport().getVehicle().getModelName()),
				() -> assertThat(estimateDetail.vehicleInfo().modelYear()).isEqualTo(result.getDamageReport().getVehicle().getModelYear()),
				() -> assertThat(estimateDetail.vehicleInfo().licenseNumber()).isEqualTo(result.getDamageReport().getVehicle().getLicenseNumber().getValue())
			);
		}

		@Test
		void 견적서를_찾을_수_없다면_예외를_반환한다() {
		    //given
			Long noSavedEstimateId = 300L;

		    //when & then
			assertThatThrownBy(() -> estimateService.findEstimateDetail(noSavedEstimateId))
				.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	@DisplayName("사고 레포트를 통한 견적서 상세 조회 기능")
	class FindEstimateDetailByDamageReportId {
		@Test
		void 사고_레포트를_통한_견적서_상세_조회_기능() {
			//given
			Long damageReportId = damageReport.getId();

			//when
			EstimateDetailResponse estimateDetail = estimateService.findEstimateDetailByDamageReportId(damageReportId);

			//then
			Estimate result = estimateRepository.findDetailByDamageReportId(damageReportId)
				.orElseThrow(() -> new AssertionError("estimate not found"));

			assertAll(
				() -> assertThat(estimateDetail.estimateId()).isEqualTo(result.getId()),
				() -> assertThat(estimateDetail.estimateStatus()).isEqualTo(result.getEstimateStatus().name()),
				() -> assertThat(estimateDetail.estimateId()).isEqualTo(result.getId()),
				() -> assertThat(estimateDetail.repairCost()).isEqualTo(result.getRepairCost()),
				() -> assertThat(estimateDetail.repairParts().size()).isEqualTo(result.getRepairParts().size())
			);
			assertAll(
				() -> assertThat(estimateDetail.damageReportInfo().isPickupRequired()).isEqualTo(result.getDamageReport().isPickupRequired()),
				() -> assertThat(estimateDetail.damageReportInfo().preferredRepairSido()).isEqualTo(result.getDamageReport().getPreferredRepairRegion().getSido()),
				() -> assertThat(estimateDetail.damageReportInfo().preferredRepairSigungu()).isEqualTo(result.getDamageReport().getPreferredRepairRegion().getSigungu()),
				() -> assertThat(estimateDetail.damageReportInfo().description()).isEqualTo(result.getDamageReport().getDescription())
			);
			assertAll(
				() -> assertThat(estimateDetail.vehicleInfo().brand()).isEqualTo(result.getDamageReport().getVehicle().getBrand()),
				() -> assertThat(estimateDetail.vehicleInfo().ownershipType()).isEqualTo(result.getDamageReport().getVehicle().getOwnershipType().name()),
				() -> assertThat(estimateDetail.vehicleInfo().ownerName()).isEqualTo(result.getDamageReport().getVehicle().getOwnerName()),
				() -> assertThat(estimateDetail.vehicleInfo().modelName()).isEqualTo(result.getDamageReport().getVehicle().getModelName()),
				() -> assertThat(estimateDetail.vehicleInfo().modelYear()).isEqualTo(result.getDamageReport().getVehicle().getModelYear()),
				() -> assertThat(estimateDetail.vehicleInfo().licenseNumber()).isEqualTo(result.getDamageReport().getVehicle().getLicenseNumber().getValue())
			);
		}

		@Test
		void 견적서를_찾을_수_없다면_예외를_반환한다() {
			//given
			Long noSavedDamageReportId = 200L;

			//when & then
			assertThatThrownBy(() -> estimateService.findEstimateDetailByDamageReportId(noSavedDamageReportId))
				.isInstanceOf(NotFoundException.class);
		}
	}
}
