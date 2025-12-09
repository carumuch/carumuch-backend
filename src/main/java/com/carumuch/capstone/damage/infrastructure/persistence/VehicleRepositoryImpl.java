package com.carumuch.capstone.damage.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carumuch.capstone.damage.domain.Vehicle;
import com.carumuch.capstone.damage.domain.VehicleRepository;

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
	public void deleteById(Long id) {
		vehicleJpaRepository.deleteById(id);
	}

	@Override
	public Optional<Vehicle> findById(Long id) {
		return vehicleJpaRepository.findById(id);
	}
}
