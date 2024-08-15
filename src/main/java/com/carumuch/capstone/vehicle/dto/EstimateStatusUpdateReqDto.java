package com.carumuch.capstone.vehicle.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "EstimateStatusUpdateReqDto: 견적 공개 범위 변경 Dto")
public class EstimateStatusUpdateReqDto {

    @NotBlank(message = "변경할 견적 공개 범위가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(PRIVATE|PUBLIC)$", message = "PRIVATE, PUBLIC 를 제외한 데이터는 올바르지 않습니다.",groups = ValidationGroups.PatternGroup.class)
    private String status;

    @Builder
    public EstimateStatusUpdateReqDto(String status) {
        this.status = status;
    }
}
