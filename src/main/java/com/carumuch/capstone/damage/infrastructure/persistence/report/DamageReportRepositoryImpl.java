package com.carumuch.capstone.damage.infrastructure.persistence.report;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DamageReportRepositoryImpl implements DamageReportRepository {

	private final JpaDamageRepository jpaDamageRepository;

	@Override
	public DamageReport save(DamageReport damageReport) {
		return jpaDamageRepository.save(damageReport);
	}

	@Override
	public void deleteById(Long id) {
		jpaDamageRepository.deleteById(id);
	}

	@Override
	public Optional<DamageReport> findById(Long id) {
		return jpaDamageRepository.findById(id);
	}
}
