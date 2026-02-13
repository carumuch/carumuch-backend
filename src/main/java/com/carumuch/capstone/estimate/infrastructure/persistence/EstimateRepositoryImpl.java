package com.carumuch.capstone.estimate.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EstimateRepositoryImpl implements EstimateRepository {

	private final JpaEstimateRepository jpaEstimateRepository;
	private final EstimateQueryRepository estimateQueryRepository;

	@Override
	public Estimate save(Estimate estimate) {
		return jpaEstimateRepository.save(estimate);
	}

	@Override
	public Optional<Estimate> findById(Long id) {
		return jpaEstimateRepository.findById(id);
	}

	@Override
	public Optional<Estimate> findDetailById(Long id) {
		return jpaEstimateRepository.findDetailById(id);
	}

	@Override
	public Optional<Estimate> findDetailByDamageReportId(Long damageReportId) {
		return jpaEstimateRepository.findDetailByDamageReportId(damageReportId);
	}

	@Override
	public Page<Estimate> searchEstimates(EstimateSearchCondition condition, Pageable pageable) {
		return estimateQueryRepository.searchEstimates(condition, pageable);
	}
}
