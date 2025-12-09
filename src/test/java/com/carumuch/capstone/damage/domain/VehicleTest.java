package com.carumuch.capstone.damage.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.support.fixture.VehicleFixture;

@ExtendWith(MockitoExtension.class)
class VehicleTest {

	@Test
	void 차량번호_양식에_맞지_않으면_예외를_반환한다() {
	    //given
		Vehicle vehicleFixture = VehicleFixture.VEHICLE_FIXTURE_1.create();

		//when & then
		Assertions.assertThatThrownBy(() -> new Vehicle(
			"가11가1111",
					VehicleOwnershipType.LEASE,
					vehicleFixture.getBrand(),
					vehicleFixture.getModelYear(),
					vehicleFixture.getModelName(),
					vehicleFixture.getOwnerName(),
					vehicleFixture.getUser()
				)
			).isInstanceOf(CustomException.class);
	}
}