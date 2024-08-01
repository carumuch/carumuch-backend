package com.carumuch.capstone.vehicle.repository;

import com.carumuch.capstone.vehicle.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * select: 차량 번호로 조회
     * 1. 차량 정보 수정
     */
    Optional<Vehicle> findByLicenseNumber(String licenseNumber);

    /**
     * select: 차량 아이디로 유저와 차량 조회 (Lazy 초기화)
     * 1. 차량 삭제
     */
    @Query("select v from Vehicle v left join fetch v.user where v.id = :id")
    Optional<Vehicle> findByIdWithUser(@Param("id") Long id);

    /**
     * 차량 번호 중복 확인
     */
    boolean existsByLicenseNumber(String licenseNumber);
}
