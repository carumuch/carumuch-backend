package com.carumuch.capstone.vehicle.dto.estimate;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "EstimateAnalysisReqDto: 견적 등록 요청 Dto")
public class EstimateAnalysisReqDto {
    @NotBlank(message = "분석 이미지 주소", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "S3에 저장된 이미지 주소 입니다.", example = "https://images")
    private String imagePath;

    public EstimateAnalysisReqDto(String imagePath) {
        this.imagePath = imagePath;
    }
}
