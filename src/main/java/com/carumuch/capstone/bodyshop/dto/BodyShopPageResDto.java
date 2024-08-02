package com.carumuch.capstone.bodyshop.dto;

import com.carumuch.capstone.bodyshop.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "BodyShopInfoResDto: 검색 공업사 목록 응답 Dto")
public class BodyShopPageResDto {
    @Schema(description = "공업사 식별자", example = "1")
    private final Long id;

    @Schema(description = "공업사 이름", example = "차박고카센터")
    private final String name;

    @Schema(description = "공업사 수리 채결 횟수", example = "1")
    private final int acceptCount;

    @Schema(description = "공업사 픽 업 가능 여부", example = "true")
    private final boolean pickupAvailability;

    private final Location location;

    @Builder
    public BodyShopPageResDto(Long id, String name, int acceptCount, boolean pickupAvailability, Location location) {
        this.id = id;
        this.name = name;
        this.acceptCount = acceptCount;
        this.pickupAvailability = pickupAvailability;
        this.location = location;
    }
}
