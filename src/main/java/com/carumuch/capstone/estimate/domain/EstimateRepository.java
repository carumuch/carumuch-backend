package com.carumuch.capstone.estimate.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query("select e from Estimate e left join fetch e.vehicle where e.id = :id")
    Optional<Estimate> findByIdWithVehicle(@Param("id") Long id);
}
