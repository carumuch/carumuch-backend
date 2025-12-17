package com.carumuch.capstone.damage.domain.report;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface DamageReportRepository {

	DamageReport save(DamageReport damageReport);

	Optional<DamageReport> findById(Long id);

	List<DamageReport> findRecent10ByUserId(Long userId);

	Page<DamageReport> findPageByUserId(Long userId, int page, int size, String sort);
}
