package com.carumuch.capstone.bodyshop.dto.bodyShop;

import com.carumuch.capstone.bodyshop.domain.Location;
import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "BodyShopUpdateReqDto: 공업사 수정 요청 Dto")
public class BodyShopUpdateReqDto {
    @NotBlank(message = "공업사 이름이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "공업사 이름 입니다.",
            example = "카우머치수리")
    private String name;

    private Location location;

    @NotBlank(message = "공업사 설명이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "공업사 설명 입니다. (최대 100자)",
            example = "홍대사거리에서 홍대동, 홍대방면으로 직진하시면 홍대교회 건너편에 위치하고 있습니다." +
                    "찾아오기 힘드시다면 바로 전화해주세요! 친철하게 안내 해드리겠습니다!")
    private String description;

    @NotBlank(message = "대표 연락처가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "대표 연락처 입니다. (하이픈 [-] 포함)",
            example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "공업사 홈페이지 링크 입니다.",
            example = "https://www.naver.com")
    private String link;

    @Schema(description = "픽업 여부 입니다.",
            example = "false")
    private boolean pickupAvailability;

    @Builder
    public BodyShopUpdateReqDto(String name, Location location, String description, String phoneNumber, String link, boolean pickupAvailability) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.link = link;
        this.pickupAvailability = pickupAvailability;
    }
}
