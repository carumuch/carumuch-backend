package com.carumuch.capstone.estimate.application;

import com.carumuch.capstone.common.exception.ForbiddenException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.presentation.dto.request.SearchEstimateRequest;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

	public PagingResponse<EstimateDetailResponse> searchEstimates(SearchEstimateRequest searchEstimateRequest, PagingRequest pagingRequest) {
		Page<Estimate> estimates = estimateRepository.searchEstimates(
			EstimateSearchCondition.from(searchEstimateRequest),
			PageRequest.of(pagingRequest.page(), pagingRequest.size(), Sort.by(pagingRequest.sort()))
		);
		return PagingResponse.from(estimates.map(EstimateDetailResponse::new));
	}
}
