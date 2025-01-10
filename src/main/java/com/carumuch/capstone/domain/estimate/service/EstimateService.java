package com.carumuch.capstone.domain.estimate.service;

import com.carumuch.capstone.domain.bid.model.type.BidStatus;
import com.carumuch.capstone.domain.bid.repository.BidRepository;
import com.carumuch.capstone.domain.estimate.dto.*;
import com.carumuch.capstone.domain.estimate.repository.EstimateRepository;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.repository.UserRepository;
import com.carumuch.capstone.domain.estimate.model.Estimate;
import com.carumuch.capstone.domain.vehicle.model.Vehicle;
import com.carumuch.capstone.domain.estimate.model.type.EstimateStatus;
import com.carumuch.capstone.domain.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carumuch.capstone.global.common.ErrorCode.BID_ALREADY_COMPLETED;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

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
               requestDto.getIsPickupRequired(),
               requestDto.getImagePath(),
               user,
               vehicle);

       return estimateRepository.save(estimate).getId();
    }

    /**
     * Update: 견적 수정
     */
    @Transactional
    public Long update(Long id, EstimateUpdateReqDto requestDto) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Estimate estimate = estimateRepository.findByIdWithUser(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 이미 매칭 된 견적인지
        if (bidRepository.existsBidByEstimateId(id, BidStatus.ACCEPTED)) {
            throw new CustomException(BID_ALREADY_COMPLETED);
        };

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

        // 이미 매칭 된 견적인지
        if (bidRepository.existsBidByEstimateId(id, BidStatus.ACCEPTED)) {
            throw new CustomException(BID_ALREADY_COMPLETED);
        };

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

        Vehicle vehicle = vehicleRepository.findByIdWithUser(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (vehicle.getUser().getLoginId().equals(loginId)) {
            Page<Estimate> estimatePage = estimateRepository
                    .findPageByVehicleId(id, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC,"createDate")));
            return estimatePage.map(estimate -> EstimateByVehiclePageResDto.builder()
                    .estimate(estimate)
                    .build());
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * Select: 유저 견적 이용 내역 조회
     */
    public Page<EstimatePageResDto> getEstimateHistory(int page) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<Estimate> estimatePage = estimateRepository
                .findPageByCreateByWithVehicle(loginId, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createDate")));
        return estimatePage.map(estimate -> EstimatePageResDto.builder()
                .estimate(estimate)
                .build());
    }

    /**
     * Update: 견적 공개 범위 수정
     */
    @Transactional
    public Long updateEstimateStatus(Long id, EstimateStatusUpdateReqDto estimateStatusUpdateReqDto) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 이미 매칭 된 견적인지
        if (bidRepository.existsBidByEstimateId(id, BidStatus.ACCEPTED)) {
            throw new CustomException(BID_ALREADY_COMPLETED);
        };

        Estimate estimate = estimateRepository.findByIdWithUser(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        if (estimate.getUser().getLoginId().equals(loginId)) {
            estimate.update(EstimateStatus.valueOf(estimateStatusUpdateReqDto.getStatus()));
            return estimate.getId();
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * Select: 나의 특정 견적 상세 조회
     */
    public EstimateDetailResDto detail(Long id) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Estimate estimate = estimateRepository.findByIdWithVehicle(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        // 나의 견적서인지 확인
        if (estimate.getCreateBy().equals(loginId)) {
            return EstimateDetailResDto.builder()
                    .estimate(estimate)
                    .build();
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * Update: Ai 비용 산정 반영 업데이트
     */
    @Transactional
    public Long updateAICost(Long id, EstimateAIRepairCostReqDto estimateAIRepairCostReqDto) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        // 나의 견적서인지 확인
        if (estimate.getCreateBy().equals(loginId)) {
            estimate.updateAIEstimatedRepairCost(estimateAIRepairCostReqDto.getAiRepairCost());
            return estimate.getId();
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }


    /**
     * info: 요구사항 변경 -> AI 통신을 server to server 로 수행하지 않는 것 으로 변경되었습니다.
     * Date: 2024.10.12
     */

    /**
     * Create: AI 견적 등록
     */
//    @Transactional
//    public Long registerAIEstimate(Long id, EstimateRegistrationReqDto requestDto) {
//        // 유저/차량 정보 조회
//        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        User user = userRepository.findLoginUserByLoginId(loginId);
//        Vehicle vehicle = vehicleRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
//
//        // TODO: AI API 요청 메서드 추가
//
//        Estimate aiEstimate = Estimate.createAIEstimate(
//                requestDto.getDescription(),
//                requestDto.getDamageArea(),
//                requestDto.getPreferredRepairSido(),
//                requestDto.getPreferredRepairSigungu(),
//                50000, // TODO: AI 연동 후 삭제
//                requestDto.getIsPickupRequired(),
//                requestDto.getImagePath(),
//                user,
//                vehicle
//        );
//        return estimateRepository.save(aiEstimate).getId();
//    }

    /**
     * AI 분석
     */
//    // TODO: AI 반환 값으로 변경
//    public void analysis(Long id, String imagePath) {
//        Vehicle vehicle = vehicleRepository.findById(id)
//                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
//        // TODO: AI API 요청 메서드 추가
//    }
}
