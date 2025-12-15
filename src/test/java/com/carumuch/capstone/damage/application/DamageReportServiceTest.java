package com.carumuch.capstone.damage.application;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;
import com.carumuch.capstone.damage.domain.vehicle.VehicleRepository;
import com.carumuch.capstone.damage.presentation.dto.request.report.RegisterDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.request.report.UpdateDamageReportRequest;
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
			Long damageReportId = 1L;
			DamageReport damageReportFixture = DamageReportFixture.DAMAGE_REPORT_FIXTURE_1.create();

			UpdateDamageReportRequest updateDamageReportRequest = new UpdateDamageReportRequest(
				"설명을 변경합니다.",
				"부산시",
				"남구",
				true
			);

			Mockito.when(damageReportRepository.findById(damageReportId)).thenReturn(Optional.of(damageReportFixture));

		    //when
			damageReportService.update(updateDamageReportRequest, damageReportId);

		    //then
		    Mockito.verify(damageReportRepository, Mockito.times(1))
				.findById(damageReportId);
		}

		@Test
		void 사고_레포트가_존재하지_않는다면_예외를_반환한다() {
		    //given
			Long damageReportId = 1L;

			UpdateDamageReportRequest updateDamageReportRequest = new UpdateDamageReportRequest(
				"설명을 변경합니다.",
				"부산시",
				"남구",
				true
			);

			Mockito.when(damageReportRepository.findById(damageReportId)).thenReturn(Optional.empty());

		    //when & then
			Assertions.assertThatThrownBy(() -> damageReportService.update(updateDamageReportRequest, damageReportId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 사고_레포트를_업데이트한다() {
		    //given
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

			Mockito.when(damageReportRepository.findById(damageReportId)).thenReturn(Optional.of(damageReport));

		    //when
			damageReportService.update(updateDamageReportRequest, damageReportId);

		    //then
			assertAll(
				() -> Assertions.assertThat(damageReport.getDescription()).isEqualTo(changeDescription),
				() -> Assertions.assertThat(damageReport.getPreferredRepairRegion().getSido()).isEqualTo(changeSido),
				() -> Assertions.assertThat(damageReport.getPreferredRepairRegion().getSigungu()).isEqualTo(changeSigungu),
				() -> Assertions.assertThat(damageReport.isPickupRequired()).isEqualTo(changeIsPickupRequired)
			);
		}
	}
}