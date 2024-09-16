package com.carumuch.capstone.bodyshop.dto.bid;


import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "BodyShopCreateBidReqDto: 공업사 입찰 생성 Dto")
public class BodyShopBidUpdateReqDto {

    @NotNull(message = "수리비가 입력되지 않았습니다.", groups = ValidationGroups.NotNullGroup.class)
    @Schema(description = "입찰을 위한 공업사 측 제안 수리비 입니다.",
            example = "300000")
    private int cost;

    @NotBlank(message = "수리 방법이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "입찰을 위한 공업사 측 수리 방법 기술입니다.",
            example = "앞 범퍼를 탈거 하여 수리 후, 도색 작업을 거쳐 다시 차량에 장착할 예정입니다.")
    private String repairMethod;

    @Builder
    public BodyShopBidUpdateReqDto(int cost, String repairMethod) {
        this.cost = cost;
        this.repairMethod = repairMethod;
    }
}

