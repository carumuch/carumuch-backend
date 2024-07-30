package com.carumuch.capstone.vehicle.controller;

import com.carumuch.capstone.vehicle.dto.VehicleRegistrationReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Vehicle")
@Validated
public interface VehicleControllerDocs {

    /* Create: 차량 등록 */
    @Operation(summary = "차량 등록 요청", description = "**성공 응답 데이터:** 차량의 `vehicle_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "차량 등록 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
            @ApiResponse(responseCode = "500", description = "서버 측 오류 발생"),
    })
    ResponseEntity<?> register(VehicleRegistrationReqDto vehicleRegistrationReqDto);
}
