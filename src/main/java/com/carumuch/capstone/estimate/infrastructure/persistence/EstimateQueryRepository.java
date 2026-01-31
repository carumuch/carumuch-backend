package com.carumuch.capstone.estimate.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;

public interface EstimateQueryRepository {
	Page<Estimate> searchEstimates(EstimateSearchCondition condition, Pageable pageable);
}
