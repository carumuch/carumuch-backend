package com.carumuch.capstone.estimate.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EstimateRepositoryImpl implements EstimateRepository {

	private final JpaEstimateRepository jpaEstimateRepository;

	@Override
	public Estimate save(Estimate estimate) {
		return jpaEstimateRepository.save(estimate);
	}

	@Override
	public Optional<Estimate> findById(Long id) {
		return jpaEstimateRepository.findById(id);
	}
}
