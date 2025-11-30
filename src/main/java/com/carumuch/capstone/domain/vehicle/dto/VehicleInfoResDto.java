package com.carumuch.capstone.domain.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "VehicleInfoResDto: 차량 상세 정보 응답 Dto")
public class VehicleInfoResDto {
    @Schema(description = "차량 식별자", example = "1")
    private final Long id;

    @Schema(description = "차량 번호", example = "00무0000")
    private final String licenseNumber; // 차량 번호

    @Schema(description = "차량 유형", example = "개인")
    private final String type; // 차량 유형 -> 법인/리스, 개인

    @Schema(description = "차량 브랜드", example = "아우디")
    private final String brand; // 차량 브랜드

    @Schema(description = "차량 연형", example = "2011")
    private final int modelYear; // 차량 연형

    @Schema(description = "차량 모델", example = "A6")
    private final String modelName;// 차량 모델

    @Schema(description = "실 소유자 명", example = "차박고")
    private final String ownerName; // 차량 실 소유자 명

    @Builder
    public VehicleInfoResDto(Long id, String licenseNumber, String type, String brand, int modelYear, String modelName, String ownerName) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.type = type;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.ownerName = ownerName;
    }
}
