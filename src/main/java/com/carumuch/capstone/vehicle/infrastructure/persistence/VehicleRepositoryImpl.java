package com.carumuch.capstone.vehicle.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.domain.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepository {

	private final VehicleJpaRepository vehicleJpaRepository;

	@Override
	public boolean existsByLicenseNumber(String licenseNumber) {
		return vehicleJpaRepository.existsByLicenseNumber(licenseNumber);
	}

	@Override
	public boolean existsByUserId(Long userId) {
		return vehicleJpaRepository.existsByUserId(userId);
	}

	@Override
	public Optional<Vehicle> findByUserId(Long userId) {
		return vehicleJpaRepository.findByUserId(userId);
	}

	@Override
	public Vehicle save(Vehicle vehicle) {
		return vehicleJpaRepository.save(vehicle);
	}

	@Override
	public void deleteById(Long userId) {
		vehicleJpaRepository.deleteById(userId);
	}

	@Override
	public Optional<Vehicle> findById(Long id) {
		return vehicleJpaRepository.findById(id);
	}
}
