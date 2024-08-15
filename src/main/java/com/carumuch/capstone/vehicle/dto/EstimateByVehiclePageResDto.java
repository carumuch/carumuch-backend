package com.carumuch.capstone.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "EstimatePageResDto: 견적 히스토리 목록 응답 Dto")
public class EstimateByVehiclePageResDto {

    @Schema(description = "견적 식별자", example = "1")
    private final Long id; // 견적 번호

    @Schema(description = "차량 손상 부위", example = "범퍼")
    private final String damageArea; // 손상 부위

    @Schema(description = "수리 희망 지역", example = "서울시")
    private final String preferredRepairSido; // 수리 희망 지역

    @Schema(description = "차량 식별자", example = "강남구")
    private final String preferredRepairSigungu; // 수리 희망 지역

    @Schema(description = "AI 예상 수리 가격", example = "50000")
    private final Integer aiEstimatedRepairCost; // AI를 통한 예상 수리 가격

    @Schema(description = "AI 를 통한 견적서 확인 여부", example = "true")
    private final boolean isAIEstimate; // AI 를 통한 견적서 확인 여부

    @Schema(description = "견적서 생성 날짜")
    private final LocalDateTime createDate;

    @Builder
    public EstimateByVehiclePageResDto(Long id, String damageArea, String preferredRepairSido, String preferredRepairSigungu, Integer aiEstimatedRepairCost, boolean isAIEstimate, LocalDateTime createDate) {
        this.id = id;
        this.damageArea = damageArea;
        this.preferredRepairSido = preferredRepairSido;
        this.preferredRepairSigungu = preferredRepairSigungu;
        this.aiEstimatedRepairCost = aiEstimatedRepairCost;
        this.isAIEstimate = isAIEstimate;
        this.createDate = createDate;
    }
}
