package com.carumuch.capstone.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@NoArgsConstructor
@Setter
@Schema(name = "EstimateSearchReqDto: 견적 상세 검색 요청 Dto")
public class EstimateSearchReqDto {

    @Schema(description = "차량 손상 부위", example = "범퍼")
    private String damageArea;

    @Schema(description = "수리 희망 지역", example = "서울시")
    private String preferredRepairSido;

    @Schema(description = "차량 식별자", example = "강남구")
    private String preferredRepairSigungu;

    @Schema(description = "AI 예상 수리 가격이 지정된 값 이하인 견적서 조회를 위한 AI 견적서의 최대 가격", example = "50000")
    private Integer aiEstimatedRepairCost;

    @Schema(description = "AI 를 통한 견적서를 모아보기 위한 값", example = "true")
    private Boolean isAIEstimate;

    @Schema(description = "차량 픽업 희망 유무", example = "true")
    private Boolean isPickupRequired;

    @Schema(description = "브랜드", example = "기아")
    private String brand;

    @Schema(description = "차량 연형", example = "2011")
    private Integer modelYear;

    @Schema(description = "차량 모델", example = "K5")
    private String modelName;

    @Schema(description = "페이지", example = "1")
    private Integer page;

    @Schema(description = "정렬 조건 (POPULAR,RECENT) / /POPULAR -> 입찰 많은 순", example = "RECENT")
    private String order;

    @Builder
    public EstimateSearchReqDto(String damageArea,
                                String preferredRepairSido,
                                String preferredRepairSigungu,
                                Integer aiEstimatedRepairCost,
                                Boolean isAIEstimate,
                                Boolean isPickupRequired,
                                String brand,
                                Integer modelYear,
                                String modelName,
                                Integer page,
                                String order) {
        this.damageArea = damageArea;
        this.preferredRepairSido = preferredRepairSido;
        this.preferredRepairSigungu = preferredRepairSigungu;
        this.aiEstimatedRepairCost = aiEstimatedRepairCost;
        this.isAIEstimate = isAIEstimate;
        this.isPickupRequired = isPickupRequired;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.page = page;
        this.order = order;
    }
}
