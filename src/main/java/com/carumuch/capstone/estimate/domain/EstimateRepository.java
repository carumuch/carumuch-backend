package com.carumuch.capstone.estimate.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;

public interface EstimateRepository {

	Estimate save(Estimate estimate);

	Optional<Estimate> findById(Long id);

	Optional<Estimate> findDetailById(Long id);

	Optional<Estimate> findDetailByDamageReportId(Long damageReportId);

	Page<Estimate> searchEstimates(EstimateSearchCondition condition, Pageable pageable);
}
