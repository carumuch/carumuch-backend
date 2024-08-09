package com.carumuch.capstone.vehicle.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "EstimateRegistrationReqDto: 일반 견적 등록 요청 Dto")
public class EstimateRegistrationReqDto {
    @NotBlank(message = "견적 상세 설명이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "견적 상세 설명 입니다.", example = "주차 중 차량 앞 범퍼 스크레치 발생")
    private String description;
    @NotBlank(message = "차량 손상 부위가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 손상 부위 입니다.", example = "범퍼")
    private String damageArea;
    @NotBlank(message = "수리 희망 시/도 가 입력 되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "수리 희망 시/도", example = "서울")
    private String preferredRepairSido;
    @NotBlank(message = "수리 희망 시군구 가 입력 되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 희망 시군구", example = "송파구")
    private String preferredRepairSigungu;
    @Schema(description = "차량 픽업 희망 여부", example = "true")
    private boolean isPickupRequired;

    @Builder
    public EstimateRegistrationReqDto(String description, String damageArea, String preferredRepairSido, String preferredRepairSigungu, boolean isPickupRequired) {
        this.description = description;
        this.damageArea = damageArea;
        this.preferredRepairSido = preferredRepairSido;
        this.preferredRepairSigungu = preferredRepairSigungu;
        this.isPickupRequired = isPickupRequired;
    }
}
