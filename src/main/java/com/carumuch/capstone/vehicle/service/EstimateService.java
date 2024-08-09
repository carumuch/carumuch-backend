package com.carumuch.capstone.vehicle.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import com.carumuch.capstone.vehicle.controller.VehicleControllerDocs;
import com.carumuch.capstone.vehicle.domain.Estimate;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.dto.EstimateRegistrationReqDto;
import com.carumuch.capstone.vehicle.dto.VehicleRegistrationReqDto;
import com.carumuch.capstone.vehicle.repository.EstimateRepository;
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
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    /**
     * Create: 견적 등록
     */
    @Transactional
    public Long register(Long id, EstimateRegistrationReqDto requestDto) {
        // 유저/차량 정보 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findLoginUserByLoginId(loginId);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

       Estimate estimate = Estimate.createBasicEstimate(
               requestDto.getDescription(),
               requestDto.getDamageArea(),
               requestDto.getPreferredRepairSido(),
               requestDto.getPreferredRepairSigungu(),
               requestDto.isPickupRequired(),
               user,
               vehicle);

       return estimateRepository.save(estimate).getId();
    }

    /**
     * Create: AI 견적 등록
     */
    @Transactional
    public Long registerAIEstimate(Long id,EstimateRegistrationReqDto requestDto) {
        // 유저/차량 정보 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findLoginUserByLoginId(loginId);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // TODO: AI API 요청 메서드 추가

        Estimate aiEstimate = Estimate.createAIEstimate(
                requestDto.getDescription(),
                requestDto.getDamageArea(),
                requestDto.getPreferredRepairSido(),
                requestDto.getPreferredRepairSigungu(),
                50000, // TODO: AI 연동 후 삭제
                requestDto.isPickupRequired(),
                user,
                vehicle
        );
        return estimateRepository.save(aiEstimate).getId();
    }


}
