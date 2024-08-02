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
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/vehicles")
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
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody VehicleUpdateReqDto vehicleUpdateReqDto,
                                    @PathVariable Long id) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, vehicleService.update(id, vehicleUpdateReqDto)));
    }

    /**
     * DELETE: 차량 삭제 요청
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, null));
    }

    /**
     * SELECT: 차량 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, vehicleService.findOne(id)));
    }

    /**
     * SELECT: 차량 목록 조회
     */
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, vehicleService.findAll()));
    }
}
