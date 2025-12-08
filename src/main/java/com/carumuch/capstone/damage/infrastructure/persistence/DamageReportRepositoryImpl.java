package com.carumuch.capstone.damage.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carumuch.capstone.damage.domain.DamageReport;
import com.carumuch.capstone.damage.domain.DamageReportRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DamageReportRepositoryImpl implements DamageReportRepository {

	private final DamageJpaRepository damageJpaRepository;

	@Override
	public DamageReport save(DamageReport damageReport) {
		return damageJpaRepository.save(damageReport);
	}

	@Override
	public void deleteById(Long id) {
		damageJpaRepository.deleteById(id);
	}

	@Override
	public Optional<DamageReport> findById(Long id) {
		return damageJpaRepository.findById(id);
	}
}
