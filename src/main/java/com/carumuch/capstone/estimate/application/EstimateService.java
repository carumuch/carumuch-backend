package com.carumuch.capstone.estimate.application;

import com.carumuch.capstone.common.exception.ForbiddenException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollQuery;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollSlice;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.presentation.dto.request.EstimateScrollRequest;
import com.carumuch.capstone.estimate.presentation.dto.request.SearchEstimateRequest;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResponse;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateScrollResponse;

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
	public void changeStatus(Long estimateId, String status, Long userId) {
		Estimate estimate = estimateRepository.findById(estimateId)
			.orElseThrow(() -> new NotFoundException(Estimate.class));
		if (!estimate.canAccess(userId)) {
			throw new ForbiddenException();
		}
		estimate.changeStatus(EstimateStatus.from(status));
	}

	public EstimateScrollResponse searchEstimates(SearchEstimateRequest searchEstimateRequest, EstimateScrollRequest scrollRequest) {
		EstimateScrollSlice<Estimate> estimates = estimateRepository.searchEstimates(
			EstimateSearchCondition.from(searchEstimateRequest),
			EstimateScrollQuery.from(scrollRequest)
		);

		return EstimateScrollResponse.from(estimates.map(EstimateDetailResponse::new));
	}
}
