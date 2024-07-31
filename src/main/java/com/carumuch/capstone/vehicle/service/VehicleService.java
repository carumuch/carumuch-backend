package com.carumuch.capstone.vehicle.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.dto.VehicleRegistrationReqDto;
import com.carumuch.capstone.vehicle.dto.VehicleUpdateReqDto;
import com.carumuch.capstone.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    /**
     * Create: 차량 등록
     */
    @Transactional
    public Long register(VehicleRegistrationReqDto requestDto) {
        checkLicenseNumberDuplicate(requestDto.getLicenseNumber());// 차량 번호 중복 확인
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        Vehicle vehicle = vehicleRepository.save(Vehicle.builder()
                .licenseNumber(requestDto.getLicenseNumber())
                .type(requestDto.getType())
                .brand(requestDto.getBrand())
                .modelName(requestDto.getModelName())
                .modelYear(requestDto.getModelYear())
                .ownerName(requestDto.getOwnerName())
                .user(user)
                .build());

        return vehicle.getId();
    }

    /* 차량 번호 중복 */
    public void checkLicenseNumberDuplicate(String licenseNumber) {
        if (vehicleRepository.existsByLicenseNumber(licenseNumber)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
    }

    /**
     * Update: 차량 정보 수정
     */
    @Transactional
    public Long update(VehicleUpdateReqDto requestDto) {
        Vehicle vehicle = vehicleRepository.findByLicenseNumber(requestDto.getLicenseNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        vehicle.update(requestDto.getLicenseNumber(),
                requestDto.getType(),
                requestDto.getBrand(),
                requestDto.getModelYear(),
                requestDto.getModelName(),
                requestDto.getOwnerName());
        return vehicle.getId();
    }


}
