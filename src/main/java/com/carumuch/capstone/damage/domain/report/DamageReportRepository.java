package com.carumuch.capstone.damage.domain.report;

import java.util.List;
import java.util.Optional;

public interface DamageReportRepository {

	DamageReport save(DamageReport damageReport);

	Optional<DamageReport> findById(Long id);

	List<DamageReport> findRecent10ByUserId(Long userId);
}
