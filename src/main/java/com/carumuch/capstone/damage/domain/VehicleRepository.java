package com.carumuch.capstone.damage.domain;

import java.util.Optional;

public interface VehicleRepository {
	boolean existsByLicenseNumber(String licenseNumber);
	boolean existsByUserId(Long userId);
	Optional<Vehicle> findByUserId(Long userId);
	Vehicle save(Vehicle vehicle);
	void deleteById(Long userId);
	Optional<Vehicle> findById(Long id);
}
