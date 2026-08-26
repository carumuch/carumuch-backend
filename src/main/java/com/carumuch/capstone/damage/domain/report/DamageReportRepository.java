package com.carumuch.capstone.damage.domain.report;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DamageReportRepository {

	DamageReport save(DamageReport damageReport);

	Optional<DamageReport> findById(Long id);

	List<DamageReport> findRecent10ByUserId(Long userId);

	Page<DamageReport> findPageByUserId(Long userId, Pageable pageable);

	Optional<DamageReport> findByIdAndUserId(Long damageReportId, Long userId);
}
