package com.carumuch.capstone.vehicle.domain;

import java.util.Optional;

public interface DamageReportRepository {
	DamageReport save(DamageReport damageReport);
	void deleteById(Long id);
	Optional<DamageReport> findById(Long id);
}
