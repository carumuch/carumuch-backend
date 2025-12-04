package com.carumuch.capstone.vehicle.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleLegacyRepository extends JpaRepository<Vehicle, Long> {

    @Query("select v from Vehicle v left join fetch v.user where v.id = :id")
    Optional<Vehicle> findByIdWithUser(@Param("id") Long id);
}
