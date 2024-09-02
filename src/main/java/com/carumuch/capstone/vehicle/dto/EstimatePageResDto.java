package com.carumuch.capstone.vehicle.dto;

import com.carumuch.capstone.vehicle.domain.Estimate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "EstimatePageResDto: 견적 목록 Dto")
public class EstimatePageResDto {
    @Schema(description = "견적 식별자", example = "1")
    private final Long id;

    @Schema(description = "차량 손상 부위", example = "범퍼")
    private final String damageArea;

    @Schema(description = "수리 희망 지역", example = "서울시")
    private final String preferredRepairSido;

    @Schema(description = "차량 식별자", example = "강남구")
    private final String preferredRepairSigungu;

    @Schema(description = "AI 예상 수리 가격", example = "50000")
    private final Integer aiEstimatedRepairCost;

    @Schema(description = "AI 를 통한 견적서 확인 여부", example = "true")
    private final boolean isAIEstimate;

    @Schema(description = "견적서 생성 날짜")
    private final LocalDateTime createDate;

    @Schema(description = "차량 번호", example = "00무0000")
    private final String licenseNumber;

    @Schema(description = "차량 유형", example = "개인")
    private final String type;

    @Schema(description = "브랜드", example = "기아")
    private final String brand;

    @Schema(description = "차량 연형", example = "2011")
    private final int modelYear;

    @Schema(description = "차량 모델", example = "K5")
    private final String modelName;

    @Schema(description = "차량 실 소유자 명", example = "차박고")
    private final String ownerName;

    @Builder
    public EstimatePageResDto(Estimate estimate) {
        this.id = estimate.getId();
        this.damageArea = estimate.getDamageArea();
        this.preferredRepairSido = estimate.getPreferredRepairSido();
        this.preferredRepairSigungu = estimate.getPreferredRepairSigungu();
        this.aiEstimatedRepairCost = estimate.getAiEstimatedRepairCost();
        this.isAIEstimate = estimate.isAIEstimate();
        this.createDate = estimate.getCreateDate();
        this.licenseNumber = estimate.getVehicle().getLicenseNumber();
        this.type = estimate.getVehicle().getType();
        this.brand = estimate.getVehicle().getBrand();
        this.modelYear = estimate.getVehicle().getModelYear();
        this.modelName = estimate.getVehicle().getModelName();
        this.ownerName = estimate.getVehicle().getOwnerName();
    }
}
