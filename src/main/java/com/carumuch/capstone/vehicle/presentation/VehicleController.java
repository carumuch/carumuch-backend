package com.carumuch.capstone.vehicle.presentation;

import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.vehicle.presentation.dto.request.RegisterVehicleRequest;
import com.carumuch.capstone.vehicle.presentation.dto.request.UpdateVehicleRequest;
import com.carumuch.capstone.vehicle.application.VehicleService;
import com.carumuch.capstone.vehicle.presentation.dto.response.VehicleInfoResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> register(@Valid @RequestBody RegisterVehicleRequest registerVehicleRequest, User user) {
        return ResponseEntity.status(OK).body(ApiResponse.of(vehicleService.register(registerVehicleRequest, user)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> update(@Valid @RequestBody UpdateVehicleRequest updateVehicleRequest, User user) {
		vehicleService.update(updateVehicleRequest, user.getId());
        return ResponseEntity.status(OK).body(ApiResponse.of());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(User user) {
        vehicleService.delete(user.getId());
        return ResponseEntity.status(OK).body(ApiResponse.of());
    }


    @GetMapping
    public ResponseEntity<ApiResponse<VehicleInfoResponse>> vehicleInfo(User user) {
        return ResponseEntity.status(OK).body(ApiResponse.of(vehicleService.info(user.getId())));
    }
}
