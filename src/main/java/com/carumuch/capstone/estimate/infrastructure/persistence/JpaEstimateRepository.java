package com.carumuch.capstone.estimate.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carumuch.capstone.estimate.domain.Estimate;

public interface JpaEstimateRepository extends JpaRepository<Estimate, Long> {
	@Query(
		"SELECT DISTINCT e FROM Estimate e "
			+ "JOIN FETCH e.damageReport dr "
			+ "JOIN FETCH dr.vehicle "
			+ "LEFT JOIN FETCH e.repairParts "
			+ "WHERE e.id = :estimateId"
	)
	Optional<Estimate> findDetailById(@Param("estimateId") Long estimateId);
}
