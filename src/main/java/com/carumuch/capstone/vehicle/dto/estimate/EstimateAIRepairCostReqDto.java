package com.carumuch.capstone.vehicle.dto.estimate;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "EstimateAIRepairCostReqDto: Ai 비용 산정 반영 Dto")
public class EstimateAIRepairCostReqDto {

    @NotNull(message = "AI 분석 결과 금액", groups = ValidationGroups.NotNullGroup.class)
    @Schema(description = "AI 비용 산정 금액입니다.", example = "530000")
    private int aiRepairCost;

    @Builder
    public EstimateAIRepairCostReqDto(int aiRepairCost) {
        this.aiRepairCost = aiRepairCost;
    }
}

