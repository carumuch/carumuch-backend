package com.carumuch.capstone.vehicle.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "VehicleDeleteReqDto: 차량 삭제 요청 Dto")
public class VehicleDeleteReqDto {
    @NotBlank(message = "차량 번호가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 번호 입니다.",
            example = "000무0000")
    private String licenseNumber;

    @Builder
    public VehicleDeleteReqDto(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
