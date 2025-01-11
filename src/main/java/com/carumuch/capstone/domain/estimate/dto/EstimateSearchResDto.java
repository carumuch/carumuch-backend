package com.carumuch.capstone.domain.estimate.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "EstimateSearchResDto: 견적 상세 검색 결과 응답 Dto")
public class EstimateSearchResDto {

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

    @Schema(description = "압철 신청자 수", example = "3")
    private final int applicantCount;

    @Schema(description = "차량 유형", example = "개인")
    private final String type;

    @Schema(description = "브랜드", example = "기아")
    private final String brand;

    @Schema(description = "차량 연형", example = "2011")
    private final int modelYear;

    @Schema(description = "차량 모델", example = "K5")
    private final String modelName;

    @Schema(description = "차량 픽업 유뮤", example = "true")
    private final boolean isPickupRequired;


    @QueryProjection
    public EstimateSearchResDto(Long id,
                                String damageArea,
                                String preferredRepairSido,
                                String preferredRepairSigungu,
                                Integer aiEstimatedRepairCost,
                                boolean isAIEstimate,
                                LocalDateTime createDate,
                                int applicantCount,
                                String type,
                                String brand,
                                int modelYear,
                                String modelName,
                                boolean isPickupRequired) {
        this.id = id;
        this.damageArea = damageArea;
        this.preferredRepairSido = preferredRepairSido;
        this.preferredRepairSigungu = preferredRepairSigungu;
        this.aiEstimatedRepairCost = aiEstimatedRepairCost;
        this.isAIEstimate = isAIEstimate;
        this.createDate = createDate;
        this.applicantCount = applicantCount;
        this.type = type;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.isPickupRequired = isPickupRequired;
    }
}
