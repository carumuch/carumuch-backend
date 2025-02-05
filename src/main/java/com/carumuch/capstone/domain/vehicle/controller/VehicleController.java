package com.carumuch.capstone.domain.vehicle.controller;

import com.carumuch.capstone.global.dto.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.domain.vehicle.dto.VehicleRegistrationReqDto;
import com.carumuch.capstone.domain.vehicle.dto.VehicleUpdateReqDto;
import com.carumuch.capstone.domain.vehicle.service.VehicleService;
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
    @PutMapping
    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody VehicleUpdateReqDto vehicleUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, vehicleService.update(vehicleUpdateReqDto)));
    }

    /**
     * DELETE: 차량 삭제 요청
     */
    @DeleteMapping
    public ResponseEntity<?> delete() {
        vehicleService.delete();
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, null));
    }

    /**
     * SELECT: 차량 상세 조회
     */
    @GetMapping("/{vehicleId}")
    public ResponseEntity<?> findById(@PathVariable("vehicleId") Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, vehicleService.findById(id)));
    }

    /**
     * SELECT: 내 차량 정보 조회
     */
    @GetMapping
    public ResponseEntity<?> vehicleInfo() {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, vehicleService.info()));
    }

    /**
     * info: 요구사항 변경 -> 차량 2대 이상에서 1대만 가지는것으로 변경 되었습니다.
     * Date: 2024.10.07
     */

    /**
     * SELECT: 차량 목록 조회
     */
//    @GetMapping
//    public ResponseEntity<?> findAll() {
//        return ResponseEntity.status(OK)
//                .body(ResponseDto.success(OK, vehicleService.findAll()));
//    }

    /**
     * DELETE: 차량 삭제 요청
     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable Long id) {
//        vehicleService.delete(id);
//        return ResponseEntity.status(CREATED)
//                .body(ResponseDto.success(CREATED, null));
//    }

    /**
     * UPDATE: 차량 정보 수정
     */
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody VehicleUpdateReqDto vehicleUpdateReqDto,
//                                    @PathVariable Long id) {
//        return ResponseEntity.status(CREATED)
//                .body(ResponseDto.success(CREATED, vehicleService.update(id, vehicleUpdateReqDto)));
//    }
}
