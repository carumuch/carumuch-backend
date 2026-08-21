package com.carumuch.capstone.estimate.infrastructure.persistence;

import com.carumuch.capstone.estimate.application.dto.EstimateScrollQuery;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollSlice;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;

public interface EstimateQueryRepository {
	EstimateScrollSlice<Estimate> searchEstimates(EstimateSearchCondition condition, EstimateScrollQuery scrollQuery);
}
