package com.carumuch.capstone.vehicle.repository;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.vehicle.domain.Estimate;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    /**
     * select: 견적 아이디로 유저와 견적 조회 (Lazy 초기화)
     * 1. 견적 삭제
     * 2. 견적 수정
     */
    @Query("select e from Estimate e left join fetch e.user where e.id = :id")
    Optional<Estimate> findByIdWithUser(@Param("id") Long id);

    /**
     * select: 견적 히스토리 차량 별 조회
     */
    @Query("select e from Estimate e left join fetch e.vehicle where e.vehicle.id = :vehicleId")
    Page<Estimate> findPageByVehicleId(@Param("vehicleId") Long id, Pageable pageable);
}
