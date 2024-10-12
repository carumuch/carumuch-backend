package com.carumuch.capstone.vehicle.dto.estimate;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * info: 요구사항 변경 -> AI 통신을 server to server 로 수행하지 않는 것 으로 변경되었습니다.
 * Date: 2024.10.12
 */
@Getter
@NoArgsConstructor
//@Schema(name = "EstimateAnalysisReqDto: 견적 분석 요청 Dto")
public class EstimateAnalysisReqDto {
//    @NotBlank(message = "분석 이미지 주소", groups = ValidationGroups.NotBlankGroup.class)
//    @Schema(description = "S3에 저장된 이미지 주소 입니다.", example = "https://images")
//    private String imagePath;
//
//    public EstimateAnalysisReqDto(String imagePath) {
//        this.imagePath = imagePath;
//    }
}
