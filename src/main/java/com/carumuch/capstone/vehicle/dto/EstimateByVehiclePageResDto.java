package com.carumuch.capstone.vehicle.dto;

import com.carumuch.capstone.vehicle.domain.Estimate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "EstimatePageResDto: 견적 차량 별 히스토리 목록 응답 Dto")
public class EstimateByVehiclePageResDto {

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

    @Builder
    public EstimateByVehiclePageResDto(Estimate estimate) {
        this.id = estimate.getId();
        this.damageArea = estimate.getDamageArea();
        this.preferredRepairSido = estimate.getPreferredRepairSido();
        this.preferredRepairSigungu = estimate.getPreferredRepairSigungu();
        this.aiEstimatedRepairCost = estimate.getAiEstimatedRepairCost();
        this.isAIEstimate = estimate.isAIEstimate();
        this.createDate = estimate.getCreateDate();
    }
}
