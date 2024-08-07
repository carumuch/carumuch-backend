package com.carumuch.capstone.vehicle.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "VehicleRegistrationReqDto: 차량 등록 요청 Dto")
public class VehicleRegistrationReqDto {

    @NotBlank(message = "차량 번호가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 번호 입니다.",
            example = "000무0000")
    private String licenseNumber;

    @NotBlank(message = "차량 유형이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 유형입니다.(개인 or 법인/리스)",
            example = "법인/리스")
    private String type;

    @NotBlank(message = "차량 브렌드가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 브렌드입니다.",
            example = "아우디")
    private String brand;

    @NotNull(message = "차량 연식이 입력되지 않았습니다.", groups = ValidationGroups.NotNullGroup.class)
    @Schema(description = "차량 연식입니다.",
            example = "2011")
    private int modelYear;

    @NotBlank(message = "차량 모델명이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 모델명 입니다.",
            example = "A6")
    private String modelName;

    @NotBlank(message = "차량 실소유자 이름이 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 실소유자 이름입니다.",
            example = "차박고")
    private String ownerName;

    @Builder
    public VehicleRegistrationReqDto(String licenseNumber, String type, String brand, int modelYear, String modelName, String ownerName) {
        this.licenseNumber = licenseNumber;
        this.type = type;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.ownerName = ownerName;
    }
}
