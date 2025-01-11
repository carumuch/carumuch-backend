package com.carumuch.capstone.domain.vehicle.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.repository.UserRepository;
import com.carumuch.capstone.domain.vehicle.model.Vehicle;
import com.carumuch.capstone.domain.vehicle.dto.VehicleInfoResDto;
import com.carumuch.capstone.domain.vehicle.dto.VehicleRegistrationReqDto;
import com.carumuch.capstone.domain.vehicle.dto.VehicleUpdateReqDto;
import com.carumuch.capstone.domain.vehicle.repository.VehicleRepository;
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
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        checkVehicleDuplicate(requestDto.getLicenseNumber(), loginId);// 차량 번호와 차량 소유 검사
        User user = userRepository.findLoginUserByLoginId(loginId);
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

    /* 차량 번호와 차량 소유 검사 */
    public void checkVehicleDuplicate(String licenseNumber, String loginId) {
        if (vehicleRepository.existsByLicenseNumber(licenseNumber)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
        if (vehicleRepository.existsByCreateBy(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
    }

    /**
     * Update: 차량 정보 수정
     */
    @Transactional
    public Long update(VehicleUpdateReqDto requestDto) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Vehicle vehicle = vehicleRepository.findByCreateBy(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        vehicle.update(requestDto.getLicenseNumber(),
                requestDto.getType(),
                requestDto.getBrand(),
                requestDto.getModelYear(),
                requestDto.getModelName(),
                requestDto.getOwnerName());
            return vehicle.getId();
    }


    /**
     * Delete: 차량 삭제 요청
     */
    @Transactional
    public void delete() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Vehicle vehicle = vehicleRepository.findByCreateBy(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        vehicle.getUser().setVehicle(null);
        vehicleRepository.deleteById(vehicle.getId());
    }

    /**
     * Select: 차량 상세 조회
     */
    public VehicleInfoResDto findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return VehicleInfoResDto.builder()
                .id(vehicle.getId())
                .licenseNumber(vehicle.getLicenseNumber())
                .type(vehicle.getType())
                .brand(vehicle.getBrand())
                .modelYear(vehicle.getModelYear())
                .modelName(vehicle.getModelName())
                .ownerName(vehicle.getOwnerName())
                .build();
    }

    /**
     * Select: 내 차량 정보 조회
     */
    public VehicleInfoResDto info() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Vehicle vehicle = vehicleRepository.findByCreateBy(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return VehicleInfoResDto.builder()
                .id(vehicle.getId())
                .licenseNumber(vehicle.getLicenseNumber())
                .type(vehicle.getType())
                .brand(vehicle.getBrand())
                .modelYear(vehicle.getModelYear())
                .modelName(vehicle.getModelName())
                .ownerName(vehicle.getOwnerName())
                .build();
    }

    /**
     * info: 요구사항 변경 -> 차량 2대 이상에서 1대만 가지는것으로 변경 되었습니다.
     * Date: 2024.10.07
     */

    /**
     * Select: 차량 목록 조회
     */
//    public List<VehicleInfoResDto> findAll() {
//        User user = userRepository
//                .findByLoginIdWithVehicle(SecurityContextHolder.getContext().getAuthentication().getName());
//
//        if (user.getVehicles().isEmpty()) {
//            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
//        }
//        return user.getVehicles().stream().map(vehicle ->
//                VehicleInfoResDto.builder()
//                        .id(vehicle.getId())
//                        .licenseNumber(vehicle.getLicenseNumber())
//                        .brand(vehicle.getBrand())
//                        .type(vehicle.getType())
//                        .modelYear(vehicle.getModelYear())
//                        .modelName(vehicle.getModelName())
//                        .ownerName(vehicle.getOwnerName())
//                        .build()
//        ).collect(Collectors.toList());
//    }

    /**
     * Update: id 로 차량 정보 수정
     */
//    @Transactional
//    public Long update(Long id, VehicleUpdateReqDto requestDto) {
//        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Vehicle vehicle = vehicleRepository.findByIdWithUser(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
//
//        if (vehicle.getUser().getLoginId().equals(loginId)) {
//            vehicle.update(requestDto.getLicenseNumber(),
//                    requestDto.getType(),
//                    requestDto.getBrand(),
//                    requestDto.getModelYear(),
//                    requestDto.getModelName(),
//                    requestDto.getOwnerName());
//            return vehicle.getId();
//        } else {
//            throw new CustomException(ErrorCode.ACCESS_DENIED);
//        }
//    }


    /**
     * Delete: id로 차량 삭제 요청
     */
//    @Transactional
//    public void deleteById(Long id) {
//        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Vehicle vehicle = vehicleRepository.findByIdWithUser(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
//
//        if (vehicle.getUser().getLoginId().equals(loginId)) {
//            vehicle.getUser().setVehicle(null);
//            vehicleRepository.deleteById(vehicle.getId());
//        } else {
//            throw new CustomException(ErrorCode.ACCESS_DENIED);
//        }
//    }
}