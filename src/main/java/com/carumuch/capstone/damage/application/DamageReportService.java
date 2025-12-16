package com.carumuch.capstone.damage.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;
import com.carumuch.capstone.damage.domain.report.RepairRegion;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.domain.vehicle.VehicleRepository;
import com.carumuch.capstone.damage.presentation.dto.request.report.RegisterDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.request.report.UpdateDamageReportRequest;
import com.carumuch.capstone.damage.presentation.dto.response.report.DamageReportInfoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DamageReportService {

	private final DamageReportRepository damageReportRepository;
	private final VehicleRepository vehicleRepository;

	@Transactional
	public Long register(RegisterDamageReportRequest requestDto, Long userId) {
		Vehicle vehicle = vehicleRepository.findByUserId(userId)
			.orElseThrow(() -> new NotFoundException(Vehicle.class));

		return damageReportRepository.save(requestDto.toEntity(vehicle)).getId();
	}

	@Transactional
	public void update(UpdateDamageReportRequest requestDto, Long damageReportId) {
		DamageReport damageReport = damageReportRepository.findById(damageReportId)
			.orElseThrow(() -> new NotFoundException(DamageReport.class));
		damageReport.update(
			requestDto.description(),
			new RepairRegion(requestDto.preferredRepairSido(), requestDto.preferredRepairSigungu()),
			requestDto.isPickupRequired()
		);
	}

	public List<DamageReportInfoResponse> findRecentReports(Long userId) {
		return damageReportRepository.findRecent10ByUserId(userId).stream()
			.map(dr -> new DamageReportInfoResponse(
				dr.getId(),
				dr.getPreferredRepairRegion().getSido(),
				dr.getPreferredRepairRegion().getSigungu(),
				dr.isPickupRequired(),
				dr.getStatus().name(),
				dr.getCreateDate()
			)).toList();
	}
}
