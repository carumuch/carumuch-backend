package com.carumuch.capstone.estimate.presentation.dto.response;

import com.carumuch.capstone.estimate.domain.Estimate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// TODO: 레거시 필드가 존재하는 DTO입니다. 리펙토링 작업 시 수정합니다.
@Getter
public class EstimateDetailResDto {
    private final Long id;
    private final String loginId;
    private final String damageArea;
    private final String description;
    private final String preferredRepairSido;
    private final String preferredRepairSigungu;
    private final Integer aiEstimatedRepairCost;
    private final boolean isAIEstimate;
    private final String imagePath;
    private final int applicantCount;
    private final boolean isPickupRequired;
    private final LocalDateTime createDate;
    private final String licenseNumber;
    private final String type;
    private final String brand;
    private final int modelYear;
    private final String modelName;
    private final String ownerName;

    @Builder
    public EstimateDetailResDto(Estimate estimate) {
        this.id = estimate.getId();
        this.loginId = estimate.getCreateBy();
        this.damageArea = estimate.getDamageArea();
        this.description = "레거시 필드로 삭제 되었습니다.";
        this.preferredRepairSido = "레거시 필드로 삭제 되었습니다.";
        this.preferredRepairSigungu = "레거시 필드로 삭제 되었습니다.";
        this.aiEstimatedRepairCost = estimate.getAiEstimatedRepairCost();
        this.isAIEstimate = false; // 레거시 필드로 삭제 되었습니다.
        this.imagePath = "레거시 필드로 삭제 되었습니다.";
        this.applicantCount = estimate.getApplicantCount();
        this.isPickupRequired = false; // 레거시 필드로 삭제 되었습니다.
        this.createDate = estimate.getCreateDate();
        this.licenseNumber = estimate.getVehicle().getLicenseNumber().getValue();
        this.type = estimate.getVehicle().getOwnershipType().name();
        this.brand = estimate.getVehicle().getBrand();
        this.modelYear = estimate.getVehicle().getModelYear();
        this.modelName = estimate.getVehicle().getModelName();
        this.ownerName = estimate.getVehicle().getOwnerName();
    }
}
