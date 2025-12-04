package com.carumuch.capstone.vehicle.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carumuch.capstone.vehicle.domain.DamageReport;

public interface DamageJpaRepository extends JpaRepository<DamageReport, Long> {

}
