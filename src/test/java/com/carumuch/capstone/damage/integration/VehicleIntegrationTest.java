package com.carumuch.capstone.damage.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;
import com.carumuch.capstone.support.IntegrationSupportTest;
import com.carumuch.capstone.support.fixture.UserFixture;
import com.carumuch.capstone.support.fixture.VehicleFixture;
import com.carumuch.capstone.damage.application.VehicleService;
import com.carumuch.capstone.damage.domain.Vehicle;
import com.carumuch.capstone.damage.domain.VehicleRepository;
import com.carumuch.capstone.damage.presentation.dto.request.RegisterVehicleRequest;
import com.carumuch.capstone.damage.presentation.dto.request.UpdateVehicleRequest;
import com.carumuch.capstone.damage.presentation.dto.response.VehicleInfoResponse;

public class VehicleIntegrationTest extends IntegrationSupportTest {

	@Autowired
	VehicleService vehicleService;
	@Autowired
	VehicleRepository vehicleRepository;
	@Autowired
	UserRepository userRepository;

	User user1;
	User user2;

	@BeforeEach
	void setUp() {
		user1 = userRepository.save(UserFixture.USER_FIXTURE_1.create());
		user2 = userRepository.save(UserFixture.USER_FIXTURE_2.create());
	}

	@Nested
	@DisplayName("차량 등록 기능")
	class Register {
		@Test
		void 차량을_등록한다() {
		    //given
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

		    //when
			Long result = vehicleService.register(registerVehicleRequest, user1);

			//then
			Assertions.assertThat(result).isInstanceOf(Long.class);
		}

		@Test
		void 이미_등록된_차량번호라면_예외를_반환한다() {
		    //given
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			Vehicle vehicle = vehicleRepository.save(
				new Vehicle(
					vehicleFixture.getLicenseNumber(),
					vehicleFixture.getOwnershipType(),
					vehicleFixture.getBrand(),
					vehicleFixture.getModelYear(),
					vehicleFixture.getModelName(),
					vehicleFixture.getOwnerName(),
					user1
				)
			);

			String duplicateLicenseNumber = vehicle.getLicenseNumber();

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				duplicateLicenseNumber,
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

			//when & then
			Assertions.assertThatThrownBy(() -> vehicleService.register(registerVehicleRequest, user2))
				.isInstanceOf(CustomException.class);
		}

		@Test
		void 등록된_사용자의_차량이_존재한다면_예외를_반환한다() {
		    //given
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			vehicleRepository.save(
				new Vehicle(
					vehicleFixture.getLicenseNumber(),
					vehicleFixture.getOwnershipType(),
					vehicleFixture.getBrand(),
					vehicleFixture.getModelYear(),
					vehicleFixture.getModelName(),
					vehicleFixture.getOwnerName(),
					user1
				)
			);

			RegisterVehicleRequest registerVehicleRequest = new RegisterVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

		    //when & then
			Assertions.assertThatThrownBy(() -> vehicleService.register(registerVehicleRequest, user1))
				.isInstanceOf(CustomException.class);
		}
	}

	@Nested
	@DisplayName("차량 업데이트 기능")
	class Update {
		@Test
		void 차량_정보를_업데이트한다() {
		    //given
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			vehicleRepository.save(
				new Vehicle(
					vehicleFixture.getLicenseNumber(),
					vehicleFixture.getOwnershipType(),
					vehicleFixture.getBrand(),
					vehicleFixture.getModelYear(),
					vehicleFixture.getModelName(),
					vehicleFixture.getOwnerName(),
					user1
				)
			);

			Vehicle updateVehicleFixture = VehicleFixture.VEHICLE_FIXTURE_2.create();
			UpdateVehicleRequest updateVehicleRequest = new UpdateVehicleRequest(
				updateVehicleFixture.getLicenseNumber(),
				updateVehicleFixture.getOwnershipType().name(),
				updateVehicleFixture.getBrand(),
				updateVehicleFixture.getModelYear(),
				updateVehicleFixture.getModelName(),
				updateVehicleFixture.getOwnerName()
			);

			//when
			vehicleService.update(updateVehicleRequest, user1.getId());

		    //then
			Vehicle vehicle = vehicleRepository.findByUserId(user1.getId())
				.orElseThrow(() -> new AssertionFailure("vehicle not found"));
			assertAll(
				() -> Assertions.assertThat(vehicle.getLicenseNumber()).isEqualTo(updateVehicleRequest.licenseNumber()),
				() -> Assertions.assertThat(vehicle.getOwnershipType().name()).isEqualTo(updateVehicleRequest.ownershipType().toUpperCase()),
				() -> Assertions.assertThat(vehicle.getBrand()).isEqualTo(updateVehicleRequest.brand()),
				() -> Assertions.assertThat(vehicle.getModelYear()).isEqualTo(updateVehicleRequest.modelYear()),
				() -> Assertions.assertThat(vehicle.getModelName()).isEqualTo(updateVehicleRequest.modelName()),
				() -> Assertions.assertThat(vehicle.getOwnerName()).isEqualTo(updateVehicleRequest.ownerName())
			);
		}

		@Test
		void 이미_등록된_차량번호라면_예외를_반환한다() {
		    //given
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			vehicleRepository.save(
				new Vehicle(
					vehicleFixture.getLicenseNumber(),
					vehicleFixture.getOwnershipType(),
					vehicleFixture.getBrand(),
					vehicleFixture.getModelYear(),
					vehicleFixture.getModelName(),
					vehicleFixture.getOwnerName(),
					user1
				)
			);

			UpdateVehicleRequest updateVehicleRequest = new UpdateVehicleRequest(
				vehicleFixture.getLicenseNumber(),
				vehicleFixture.getOwnershipType().name(),
				vehicleFixture.getBrand(),
				vehicleFixture.getModelYear(),
				vehicleFixture.getModelName(),
				vehicleFixture.getOwnerName()
			);

		    //when & then
			Assertions.assertThatThrownBy(() -> vehicleService.update(updateVehicleRequest, user1.getId()))
				.isInstanceOf(CustomException.class);
		}
	}

	@Nested
	@DisplayName("차량 삭제 기능")
	class Delete {
		@Test
		void 차량을_삭제한다() {
		    //given
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			Vehicle vehicle = vehicleRepository.save(
				new Vehicle(
					vehicleFixture.getLicenseNumber(),
					vehicleFixture.getOwnershipType(),
					vehicleFixture.getBrand(),
					vehicleFixture.getModelYear(),
					vehicleFixture.getModelName(),
					vehicleFixture.getOwnerName(),
					user1
				)
			);

			//when
			vehicleService.delete(user1.getId());

		    //then
			Assertions.assertThat(vehicleRepository.findById(vehicle.getId())).isEqualTo(Optional.empty());
		}

		@Test
		void 사용자의_등록된_차량이_없다면_예외를_반환한다() {
		    //given
			Long userId = user1.getId();

		    //when & then
			Assertions.assertThatThrownBy(() -> vehicleService.delete(userId))
				.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	@DisplayName("차량 정보 조회 기능")
	class Info {
		@Test
		void 차량_정보를_조회한다() {
		    //given
			Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();
			Vehicle vehicle = vehicleRepository.save(
				new Vehicle(
					vehicleFixture.getLicenseNumber(),
					vehicleFixture.getOwnershipType(),
					vehicleFixture.getBrand(),
					vehicleFixture.getModelYear(),
					vehicleFixture.getModelName(),
					vehicleFixture.getOwnerName(),
					user1
				)
			);

		    //when
			VehicleInfoResponse result = vehicleService.info(user1.getId());

			//then
			assertAll(
				() -> Assertions.assertThat(result.id()).isEqualTo(vehicle.getId()),
				() -> Assertions.assertThat(result.ownershipType()).isEqualTo(vehicle.getOwnershipType().name()),
				() -> Assertions.assertThat(result.modelName()).isEqualTo(vehicle.getModelName()),
				() -> Assertions.assertThat(result.modelYear()).isEqualTo(vehicle.getModelYear()),
				() -> Assertions.assertThat(result.brand()).isEqualTo(vehicle.getBrand()),
				() -> Assertions.assertThat(result.ownerName()).isEqualTo(vehicle.getOwnerName())
			);
		}

		@Test
		void 사용자의_등록된_차량이_없다면_예외를_반환한다() {
		    //given
			Long userId = user1.getId();

		    //when & then
			Assertions.assertThatThrownBy(() -> vehicleService.info(userId))
				.isInstanceOf(NotFoundException.class);
		}
	}

}
