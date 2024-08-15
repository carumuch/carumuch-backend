package com.carumuch.capstone.vehicle.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.image.service.ImageService;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import com.carumuch.capstone.vehicle.domain.Estimate;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.dto.EstimateByVehiclePageResDto;
import com.carumuch.capstone.vehicle.dto.EstimateRegistrationReqDto;
import com.carumuch.capstone.vehicle.dto.EstimateUpdateReqDto;
import com.carumuch.capstone.vehicle.repository.EstimateRepository;
import com.carumuch.capstone.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    /**
     * Create: 견적 등록
     */
    @Transactional
    public Long register(Long id, MultipartFile image,EstimateRegistrationReqDto requestDto) {
        // 유저/차량 정보 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findLoginUserByLoginId(loginId);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 이미지 저장
        String imagePath = imageService.uploadImage(image);

        Estimate estimate = Estimate.createBasicEstimate(
               requestDto.getDescription(),
               requestDto.getDamageArea(),
               requestDto.getPreferredRepairSido(),
               requestDto.getPreferredRepairSigungu(),
               requestDto.isPickupRequired(),
               image.getOriginalFilename(),
               imagePath,
               user,
               vehicle);

       return estimateRepository.save(estimate).getId();
    }

    /**
     * Create: AI 견적 등록
     */
    @Transactional
    public Long registerAIEstimate(Long id, MultipartFile image, EstimateRegistrationReqDto requestDto) {
        // 유저/차량 정보 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findLoginUserByLoginId(loginId);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 이미지 저장
        String imagePath = imageService.uploadImage(image);

        // TODO: AI API 요청 메서드 추가

        Estimate aiEstimate = Estimate.createAIEstimate(
                requestDto.getDescription(),
                requestDto.getDamageArea(),
                requestDto.getPreferredRepairSido(),
                requestDto.getPreferredRepairSigungu(),
                50000, // TODO: AI 연동 후 삭제
                requestDto.isPickupRequired(),
                image.getOriginalFilename(),
                imagePath,
                user,
                vehicle
        );
        return estimateRepository.save(aiEstimate).getId();
    }

    /**
     * AI 분석
     */
    // TODO: AI 반환 값으로 변경
    public void analysis(Long id, MultipartFile image) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        // TODO: AI API 요청 메서드 추가
    }

    /**
     * Update: 견적 수정
     */
    @Transactional
    public Long update(Long id, EstimateUpdateReqDto requestDto) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Estimate estimate = estimateRepository.findByIdWithUser(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (estimate.getUser().getLoginId().equals(loginId)) {
            estimate.update(requestDto.getDescription(),
                    requestDto.getDamageArea(),
                    requestDto.getPreferredRepairSido(),
                    requestDto.getPreferredRepairSigungu(),
                    requestDto.isPickupRequired());
            return estimate.getId();
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * Delete: 견적 삭제
     */
    @Transactional
    public void delete(Long id) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Estimate estimate = estimateRepository.findByIdWithUser(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        if (estimate.getUser().getLoginId().equals(loginId)) {
            estimateRepository.deleteById(id);
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * Select: 견적 히스토리 차량별 조회
     */
    public Page<EstimateByVehiclePageResDto> getEstimateHistoryByVehicleId(int page, Long id) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Estimate estimate = estimateRepository.findByIdWithUser(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        if (estimate.getUser().getLoginId().equals(loginId)) {
            Page<Estimate> estimatePage = estimateRepository
                    .findPageByVehicleId(id, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC,"createDate")));
            return estimatePage.map(e -> EstimateByVehiclePageResDto.builder()
                    .id(e.getId())
                    .damageArea(e.getDamageArea())
                    .preferredRepairSido(e.getPreferredRepairSido())
                    .preferredRepairSigungu(e.getPreferredRepairSigungu())
                    .isAIEstimate(e.isAIEstimate())
                    .aiEstimatedRepairCost(e.getAiEstimatedRepairCost())
                    .createDate(e.getCreateDate())
                    .build());
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
}
