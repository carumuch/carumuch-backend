package com.carumuch.capstone.bodyshop.domain;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(name = "Location: 공업사 위치 데이터")
public class Location {

    @NotBlank(message = "시/도 가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "시/도 입니다.",
            example = "서울특별시")
    @Column(name = "sido")
    private String sido;

    @NotBlank(message = "시/군/구 가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "시/군/구 입니다.",
            example = "송파구")
    @Column(name = "sigungu")
    private String siqungu;

    @NotBlank(message = "법정동명이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "법정동명 입니다.",
            example = "잠실동")
    @Column(name = "bname")
    private String bname;

    @NotBlank(message = "지번이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "지번 입니다.",
            example = "서울 송파구 잠실동 123-45")
    @Column(name = "jibunAddress")
    private String jibunAddress;

    @NotBlank(message = "도로명 주소가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "도로명 주소 입니다.",
            example = "서울 송파구 잠실로12길 12-34")
    @Column(name = "roadAddress")
    private String roadAddress;

    @Schema(description = "상세 주소 입니다.",
            example = "차박고빌딩 101동 1층")
    @Column(name = "sangse")
    private String sangse;

    public Location(String sido, String siqungu, String bname, String jibunAddress, String roadAddress, String sangse) {
        this.sido = sido;
        this.siqungu = siqungu;
        this.bname = bname;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.sangse = sangse;
    }
}
