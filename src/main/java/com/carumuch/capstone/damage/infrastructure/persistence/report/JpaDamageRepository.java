package com.carumuch.capstone.damage.infrastructure.persistence.report;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carumuch.capstone.damage.domain.report.DamageReport;

public interface JpaDamageRepository extends JpaRepository<DamageReport, Long> {

	@Query(
		"SELECT dr FROM DamageReport dr "
			+ "JOIN dr.vehicle v "
			+ "WHERE v.user.id = :userId"
	)
	List<DamageReport> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);

	@Query(
		"SELECT dr FROM DamageReport dr "
			+ "JOIN dr.vehicle v "
			+ "WHERE v.user.id = :userId"
	)
	Page<DamageReport> findPageByUserId(@Param("userId") Long userId, Pageable pageable);

	@Query(
		"SELECT dr FROM DamageReport dr "
			+ "JOIN dr.vehicle v "
			+ "WHERE dr.id = :damageReportId "
			+ "AND v.user.id = :userId"
	)
	Optional<DamageReport> findByIdAndUserId(@Param("damageReportId") Long damageReportId, @Param("userId") Long userId);
}
