package com.carumuch.capstone.vehicle.application;

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

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.support.fixture.UserFixture;
import com.carumuch.capstone.support.fixture.VehicleFixture;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.domain.VehicleRepository;
import com.carumuch.capstone.vehicle.presentation.dto.request.RegisterVehicleRequest;
import com.carumuch.capstone.vehicle.presentation.dto.request.UpdateVehicleRequest;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

	@InjectMocks VehicleService vehicleService;
	@Mock VehicleRepository vehicleRepository;

	@Nested
	@DisplayName("차량 등록 기능")
	class Register {
		@Test
		void 차량번호_중복을_확인한다() {
		    //given
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			Mockito.when(vehicleRepository.existsByLicenseNumber(vehicleFixture.getLicenseNumber())).thenReturn(false);

			Vehicle vehicle = registerVehicleRequest.toEntity(userFixture);
			ReflectionTestUtils.setField(vehicle, "id", 1L);
			Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenReturn(vehicle);

		    //when
			vehicleService.register(registerVehicleRequest, userFixture);

		    //then
		    Mockito.verify(vehicleRepository, Mockito.times(1))
				.existsByLicenseNumber(vehicleFixture.getLicenseNumber());
		}

		@Test
		void 차량번호_중복시_예외를_반환한다() {
		    //given
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			Mockito.when(vehicleRepository.existsByLicenseNumber(vehicleFixture.getLicenseNumber())).thenReturn(true);

			//when & then
			Assertions.assertThatThrownBy(() -> vehicleService.register(registerVehicleRequest, userFixture))
				.isInstanceOf(CustomException.class);

		}

		@Test
		void 사용자가_이미_차량을_등록했는지_확인한다() {
		    //given
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			ReflectionTestUtils.setField(userFixture, "id", 1L);

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);
			Mockito.when(vehicleRepository.existsByUserId(userFixture.getId())).thenReturn(false);

			Vehicle vehicle = registerVehicleRequest.toEntity(userFixture);
			ReflectionTestUtils.setField(vehicle, "id", 1L);
			Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenReturn(vehicle);

		    //when
			vehicleService.register(registerVehicleRequest, userFixture);

		    //then
		    Mockito.verify(vehicleRepository, Mockito.times(1))
				.existsByUserId(userFixture.getId());
		}

		@Test
		void 사용자가_이미_차량을_등록했다면_예외를_반환한다() {
		    //given
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			ReflectionTestUtils.setField(userFixture, "id", 1L);

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);
			Mockito.when(vehicleRepository.existsByUserId(userFixture.getId())).thenReturn(true);

		    //when & then
			Assertions.assertThatThrownBy(() -> vehicleService.register(registerVehicleRequest, userFixture))
				.isInstanceOf(CustomException.class);
		}

		@Test
		void 차량을_등록한다() {
		    //given
			User userFixture = UserFixture.USER_FIXTURE_1.create();
			ReflectionTestUtils.setField(userFixture, "id", 1L);

			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			Vehicle newVehicle = registerVehicleRequest.toEntity(userFixture);
			ReflectionTestUtils.setField(newVehicle, "id", 1L);
			Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class)))
				.thenReturn(newVehicle);

		    //when
			Long result = vehicleService.register(registerVehicleRequest, userFixture);

			//then
		    Assertions.assertThat(result).isEqualTo(1L);
		}
	}
	
	@Nested
	@DisplayName("차량 정보 업데이트 기능")
	class Update {
		@Test
		void 사용자의_차량을_조회한다() {
		    //given
			String changeLicenseNumber = "777가7777";
			String changeOwnershipType = "corporate";
			String changeBrand = "페라리";
			Integer changeModelYear = 2025;
			String changeModelName = "모델이름";
			String changeOwnerName = "이순신";

			Long userId = 1L;
			Vehicle vehicle = VehicleFixture.VEHICLE_FIXTURE_1.create();
			UpdateVehicleRequest updateVehicleRequest = new UpdateVehicleRequest(
				changeLicenseNumber,
				changeOwnershipType,
				changeBrand,
				changeModelYear,
				changeModelName,
				changeOwnerName
			);

			Mockito.when(vehicleRepository.findByUserId(userId))
				.thenReturn(Optional.of(vehicle));

			//when
		    vehicleService.update(updateVehicleRequest, userId);

		    //then
		    Mockito.verify(vehicleRepository, Mockito.times(1))
				.findByUserId(userId);
		}
		
		@Test
		void 사용자의_차량이_없다면_예외를_반환한다() {
		    //given
			String changeLicenseNumber = "777가7777";
			String changeOwnershipType = "corporate";
			String changeBrand = "페라리";
			Integer changeModelYear = 2025;
			String changeModelName = "모델이름";
			String changeOwnerName = "이순신";

			Long userId = 1L;
			Vehicle vehicle = VehicleFixture.VEHICLE_FIXTURE_1.create();
			UpdateVehicleRequest updateVehicleRequest = new UpdateVehicleRequest(
				changeLicenseNumber,
				changeOwnershipType,
				changeBrand,
				changeModelYear,
				changeModelName,
				changeOwnerName
			);

			Mockito.when(vehicleRepository.findByUserId(userId))
				.thenReturn(Optional.empty());
		    
		    //when & then
			Assertions.assertThatThrownBy(() -> vehicleService.update(updateVehicleRequest, userId))
				.isInstanceOf(NotFoundException.class);
		    
		}

		@Test
		void 차량정보를_업데이트_한다() {
		    //given
			String changeLicenseNumber = "777가7777";
			String changeOwnershipType = "corporate";
			String changeBrand = "페라리";
			Integer changeModelYear = 2025;
			String changeModelName = "모델이름";
			String changeOwnerName = "이순신";

			Long userId = 1L;
			Vehicle vehicle = VehicleFixture.VEHICLE_FIXTURE_1.create();
			UpdateVehicleRequest updateVehicleRequest = new UpdateVehicleRequest(
				changeLicenseNumber,
				changeOwnershipType,
				changeBrand,
				changeModelYear,
				changeModelName,
				changeOwnerName
			);
			Mockito.when(vehicleRepository.findByUserId(userId))
				.thenReturn(Optional.of(vehicle));

		    //when
			vehicleService.update(updateVehicleRequest, userId);

		    //then
			assertAll(
				() -> Assertions.assertThat(vehicle.getLicenseNumber()).isEqualTo(changeLicenseNumber),
				() -> Assertions.assertThat(vehicle.getOwnershipType().name()).isEqualTo(changeOwnershipType.toUpperCase()),
				() -> Assertions.assertThat(vehicle.getBrand()).isEqualTo(changeBrand),
				() -> Assertions.assertThat(vehicle.getModelYear()).isEqualTo(changeModelYear),
				() -> Assertions.assertThat(vehicle.getModelName()).isEqualTo(changeModelName),
				() -> Assertions.assertThat(vehicle.getOwnerName()).isEqualTo(changeOwnerName)
			);
		}
	}
}