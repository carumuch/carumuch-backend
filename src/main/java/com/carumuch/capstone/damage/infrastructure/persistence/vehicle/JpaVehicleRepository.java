package com.carumuch.capstone.damage.infrastructure.persistence.vehicle;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carumuch.capstone.damage.domain.vehicle.LicenseNumber;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;

public interface JpaVehicleRepository extends JpaRepository<Vehicle, Long> {
	boolean existsByLicenseNumber(LicenseNumber licenseNumber);
	boolean existsByUserId(Long userId);
	Optional<Vehicle> findByUserId(Long userId);
}
