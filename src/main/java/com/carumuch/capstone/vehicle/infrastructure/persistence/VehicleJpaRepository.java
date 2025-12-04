package com.carumuch.capstone.vehicle.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carumuch.capstone.vehicle.domain.Vehicle;

public interface VehicleJpaRepository extends JpaRepository<Vehicle, Long> {
	boolean existsByLicenseNumber(String licenseNumber);
	boolean existsByUserId(Long userId);
	Optional<Vehicle> findByUserId(Long userId);
}
