package com.carumuch.capstone.damage.infrastructure.persistence.report;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carumuch.capstone.damage.domain.report.DamageReport;

public interface JpaDamageRepository extends JpaRepository<DamageReport, Long> {

}
