package com.carumuch.capstone.damage.application;

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.damage.domain.LicenseNumber;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.damage.domain.Vehicle;
import com.carumuch.capstone.damage.domain.VehicleOwnershipType;
import com.carumuch.capstone.damage.domain.VehicleRepository;
import com.carumuch.capstone.damage.presentation.dto.response.VehicleInfoResponse;
import com.carumuch.capstone.damage.presentation.dto.request.RegisterVehicleRequest;
import com.carumuch.capstone.damage.presentation.dto.request.UpdateVehicleRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleService {
	private static final String LICENSE_NUMBER_DUPLICATE_MESSAGE = "이미 등록된 차량 번호입니다.";
	private static final String VEHICLE_ALREADY_EXIST_MESSAGE = "이미 등록된 차량이 존재합니다.";

    private final VehicleRepository vehicleRepository;

    @Transactional
    public Long register(RegisterVehicleRequest requestDto, User user) {
        checkDuplicateVehicle(requestDto.licenseNumber(), user.getId());
        return vehicleRepository.save(requestDto.toEntity(user)).getId();
    }

	private void checkDuplicateVehicle(String licenseNumber, Long userId) {
		checkDuplicateLicenseNumber(licenseNumber);
		checkVehicleAlreadyExists(userId);
	}

	private void checkDuplicateLicenseNumber(String licenseNumber) {
		if (vehicleRepository.existsByLicenseNumber(new LicenseNumber(licenseNumber))) {
			throw new CustomException(HttpStatus.CONFLICT, LICENSE_NUMBER_DUPLICATE_MESSAGE);
		}
	}

	private void checkVehicleAlreadyExists(Long userId) {
		if (vehicleRepository.existsByUserId(userId)) {
			throw new CustomException(HttpStatus.CONFLICT, VEHICLE_ALREADY_EXIST_MESSAGE);
		}
	}

    @Transactional
    public void update(UpdateVehicleRequest requestDto, Long userId) {
		checkDuplicateLicenseNumber(requestDto.licenseNumber());

		Vehicle vehicle = vehicleRepository.findByUserId(userId)
			.orElseThrow(() -> new NotFoundException(Vehicle.class));
		vehicle.update(
			requestDto.licenseNumber(),
			VehicleOwnershipType.from(requestDto.ownershipType()),
			requestDto.brand(),
			requestDto.modelYear(),
			requestDto.modelName(),
			requestDto.ownerName()
		);
    }

    @Transactional
    public void delete(Long userId) {
        Vehicle vehicle = vehicleRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Vehicle.class));
        vehicleRepository.deleteById(vehicle.getId());
    }

    public VehicleInfoResponse info(Long userId) {
        return vehicleRepository.findByUserId(userId)
			.map(VehicleInfoResponse::from)
			.orElseThrow(() -> new NotFoundException(Vehicle.class));
    }
}