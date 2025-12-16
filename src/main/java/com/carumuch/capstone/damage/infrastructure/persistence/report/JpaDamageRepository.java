package com.carumuch.capstone.damage.infrastructure.persistence.report;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carumuch.capstone.damage.domain.report.DamageReport;

public interface JpaDamageRepository extends JpaRepository<DamageReport, Long> {

	@Query(
		"SELECT dr FROM DamageReport dr "
			+ "JOIN dr.vehicle v "
			+ "WHERE v.user.id = :userId "
	)
	List<DamageReport> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);
}
