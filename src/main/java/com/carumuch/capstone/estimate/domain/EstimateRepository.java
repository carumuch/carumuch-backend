package com.carumuch.capstone.estimate.domain;

import java.util.Optional;

public interface EstimateRepository {

	Estimate save(Estimate estimate);

	Optional<Estimate> findById(Long id);

	Optional<Estimate> findDetailById(Long id);
}
