package com.carumuch.capstone.estimate.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carumuch.capstone.estimate.domain.Estimate;

public interface JpaEstimateRepository extends JpaRepository<Estimate, Long> {
}
