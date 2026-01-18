package com.carumuch.capstone.estimate.application;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateService {
    private final EstimateRepository estimateRepository;

	public EstimateDetailResponse findEstimateDetail(Long estimateId) {
		return estimateRepository.findDetailById(estimateId)
			.map(EstimateDetailResponse::new)
			.orElseThrow(() -> new NotFoundException(Estimate.class));
	}

	public EstimateDetailResponse findEstimateDetailByDamageReportId(Long damageReportId) {
		return estimateRepository.findDetailByDamageReportId(damageReportId)
			.map(EstimateDetailResponse::new)
			.orElseThrow(() -> new NotFoundException(Estimate.class));
	}

	@Transactional
	public void changeStatus(Long estimateId, String status) {
		Estimate estimate = estimateRepository.findById(estimateId)
			.orElseThrow(() -> new NotFoundException(Estimate.class));
		estimate.updateStatus(EstimateStatus.from(status));
	}
}
