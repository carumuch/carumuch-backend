package com.carumuch.capstone.damage.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carumuch.capstone.damage.domain.Vehicle;

public interface VehicleJpaRepository extends JpaRepository<Vehicle, Long> {
	boolean existsByLicenseNumber(String licenseNumber);
	boolean existsByUserId(Long userId);
	Optional<Vehicle> findByUserId(Long userId);
}
