package com.carumuch.capstone.damage.infrastructure.persistence.report;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DamageReportRepositoryImpl implements DamageReportRepository {
	private static final int DEFAULT_RECENT_SIZE = 10;
	private static final String DEFAULT_SORT_FIELD = "createDate";

	private final JpaDamageRepository jpaDamageRepository;

	@Override
	public DamageReport save(DamageReport damageReport) {
		return jpaDamageRepository.save(damageReport);
	}

	@Override
	public Optional<DamageReport> findById(Long id) {
		return jpaDamageRepository.findById(id);
	}

	@Override
	public List<DamageReport> findRecent10ByUserId(Long userId) {
		PageRequest pageRequest = PageRequest.of(
			0,
			DEFAULT_RECENT_SIZE,
			Sort.by(Sort.Direction.DESC, DEFAULT_SORT_FIELD)
		);
		return jpaDamageRepository.findRecentByUserId(userId, pageRequest);
	}

	@Override
	public Page<DamageReport> findPageByUserId(Long userId, int page, int size, String sort) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
		return jpaDamageRepository.findPageByUserId(userId, pageRequest);
	}

	@Override
	public Optional<DamageReport> findByIdAndUserId(Long damageReportId, Long userId) {
		return jpaDamageRepository.findByIdAndUserId(damageReportId, userId);
	}
}
