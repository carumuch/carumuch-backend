package com.carumuch.capstone.vehicle.repository;

import com.carumuch.capstone.vehicle.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * select: 차량 번호로 조회
     * 1. 차량 정보 수정
     */
    Optional<Vehicle> findByLicenseNumber(String licenseNumber);
}
