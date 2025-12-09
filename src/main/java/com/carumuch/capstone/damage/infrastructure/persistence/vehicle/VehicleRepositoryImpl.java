package com.carumuch.capstone.damage.infrastructure.persistence.vehicle;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carumuch.capstone.damage.domain.vehicle.LicenseNumber;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.domain.vehicle.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepository {

	private final JpaVehicleRepository jpaVehicleRepository;

	@Override
	public boolean existsByLicenseNumber(LicenseNumber licenseNumber) {
		return jpaVehicleRepository.existsByLicenseNumber(licenseNumber);
	}

	@Override
	public boolean existsByUserId(Long userId) {
		return jpaVehicleRepository.existsByUserId(userId);
	}

	@Override
	public Optional<Vehicle> findByUserId(Long userId) {
		return jpaVehicleRepository.findByUserId(userId);
	}

	@Override
	public Vehicle save(Vehicle vehicle) {
		return jpaVehicleRepository.save(vehicle);
	}

	@Override
	public void deleteById(Long id) {
		jpaVehicleRepository.deleteById(id);
	}

	@Override
	public Optional<Vehicle> findById(Long id) {
		return jpaVehicleRepository.findById(id);
	}
}
