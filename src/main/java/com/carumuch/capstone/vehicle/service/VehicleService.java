package com.carumuch.capstone.vehicle.service;

import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import com.carumuch.capstone.vehicle.domain.Vehicle;
import com.carumuch.capstone.vehicle.dto.VehicleRegistrationReqDto;
import com.carumuch.capstone.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long register(VehicleRegistrationReqDto requestDto) {
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        Vehicle vehicle = vehicleRepository.save(
                Vehicle.builder()
                .licenseNumber(requestDto.getLicenseNumber())
                .type(requestDto.getType())
                .brand(requestDto.getBrand())
                .modelName(requestDto.getModelName())
                .modelYear(requestDto.getModelYear())
                .ownerName(requestDto.getOwnerName())
                .build());
        vehicle.setUser(user);
        return vehicle.getId();
    }
}
