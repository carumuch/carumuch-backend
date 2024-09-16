package com.carumuch.capstone.bodyshop.dto.bodyShop;

import com.carumuch.capstone.bodyshop.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "BodyShopInfoResDto: 검색 공업사 목록 응답 Dto")
public class BodyShopInfoResDto {

    @Schema(description = "공업사 식별자", example = "1")
    private final Long id;

    @Schema(description = "공업사 이름", example = "차박고카센터")
    private final String name;

    private final Location location;

    @Schema(description = "공업사 설명", example = "안녕하세요.")
    private final String description;

    @Schema(description = "공업사 전화 번호", example = "010-1234-5678")
    private final String phoneNumber;

    @Schema(description = "공업사 홈페이지", example = "https://naver.com")
    private final String link;

    @Schema(description = "공업사 수리 치결 수", example = "3")
    private final int acceptCount;

    @Schema(description = "공업사 픽 업 가능 여부", example = "true")
    private final boolean pickupAvailability; // 픽 업 가능 여부

    @Builder
    public BodyShopInfoResDto(Long id, String name, Location location, String description, String phoneNumber, String link, int acceptCount, boolean pickupAvailability) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.link = link;
        this.acceptCount = acceptCount;
        this.pickupAvailability = pickupAvailability;
    }
}
