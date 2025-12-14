package com.carumuch.capstone.damage.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.damage.domain.vehicle.VehicleRepository;
import com.carumuch.capstone.damage.presentation.dto.request.report.RegisterDamageReportRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DamageReportService {

	private final DamageReportRepository damageReportRepository;
	private final VehicleRepository vehicleRepository;

	@Transactional
	public Long register(RegisterDamageReportRequest requestDto, Long userId) {
		Vehicle vehicle = vehicleRepository.findByUserId(userId)
			.orElseThrow(() -> new NotFoundException(Vehicle.class));

		return damageReportRepository.save(requestDto.toEntity(vehicle)).getId();
	}
}
