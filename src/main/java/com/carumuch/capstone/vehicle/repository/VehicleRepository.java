package com.carumuch.capstone.vehicle.repository;

import com.carumuch.capstone.vehicle.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
