package com.carumuch.capstone.damage.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carumuch.capstone.damage.domain.DamageReport;

public interface DamageJpaRepository extends JpaRepository<DamageReport, Long> {

}
