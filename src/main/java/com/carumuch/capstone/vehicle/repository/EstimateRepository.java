package com.carumuch.capstone.vehicle.repository;

import com.carumuch.capstone.vehicle.domain.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
}
