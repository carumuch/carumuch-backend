package com.carumuch.capstone.domain.estimate.dto;


import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "EstimateUpdateReqDto: 견적 수정 요청 Dto")
public class EstimateUpdateReqDto {
    @NotBlank(message = "수정 할 견적 상세 설명이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "견적 상세 설명 입니다.", example = "후면 주차 중 차량 뒷 범퍼 미세한 스크레치 발생")
    private String description;
    @NotBlank(message = "수정 할 차량 손상 부위가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 손상 부위 입니다.", example = "뒷 범퍼")
    private String damageArea;
    @NotBlank(message = "수정 할 수리 희망 시/도 가 입력 되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "수리 희망 시/도", example = "강원도")
    private String preferredRepairSido;
    @NotBlank(message = "수정 할 수리 희망 시군구 가 입력 되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "차량 희망 시군구", example = "춘천시")
    private String preferredRepairSigungu;
    @Schema(description = "차량 픽업 희망 여부", example = "false")
    private boolean isPickupRequired;

    @Builder
    public EstimateUpdateReqDto(String description, String damageArea, String preferredRepairSido, String preferredRepairSigungu, boolean isPickupRequired) {
        this.description = description;
        this.damageArea = damageArea;
        this.preferredRepairSido = preferredRepairSido;
        this.preferredRepairSigungu = preferredRepairSigungu;
        this.isPickupRequired = isPickupRequired;
    }
}

