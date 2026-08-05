package com.carumuch.capstone.estimate.domain;

import java.util.Optional;

import com.carumuch.capstone.estimate.application.dto.EstimateScrollQuery;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollSlice;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;

public interface EstimateRepository {

	Estimate save(Estimate estimate);

	Optional<Estimate> findById(Long id);

	Optional<Estimate> findDetailById(Long id);

	Optional<Estimate> findDetailByDamageReportId(Long damageReportId);

	EstimateScrollSlice<Estimate> searchEstimates(EstimateSearchCondition condition, EstimateScrollQuery scrollQuery);
}
