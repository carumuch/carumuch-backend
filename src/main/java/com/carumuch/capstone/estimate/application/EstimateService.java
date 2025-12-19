package com.carumuch.capstone.estimate.application;

import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResDto;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import com.carumuch.capstone.common.legacy.exception.ErrorCode;
import com.carumuch.capstone.common.legacy.exception.CustomException;
import com.carumuch.capstone.estimate.domain.Estimate;

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
}
