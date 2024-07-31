package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.vehicle.dto.VehicleRegistrationReqDto;
import com.carumuch.capstone.vehicle.dto.VehicleUpdateReqDto;
import com.carumuch.capstone.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController implements VehicleControllerDocs {

    private final VehicleService vehicleService;

    /**
     * CREATE: 차량 등록
     */
    @PostMapping
    public ResponseEntity<?> register(@Validated(ValidationSequence.class) @RequestBody VehicleRegistrationReqDto vehicleRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, vehicleService.register(vehicleRegistrationReqDto)));
    }

    /**
     * UPDATE: 차량 정보 수정
     */
    @PutMapping
    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody VehicleUpdateReqDto vehicleUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, vehicleService.update(vehicleUpdateReqDto)));
    }
}