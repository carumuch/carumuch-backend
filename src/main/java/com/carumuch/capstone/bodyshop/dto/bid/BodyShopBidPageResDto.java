package com.carumuch.capstone.bodyshop.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "BodyShopBidPageResDto: 공업사 측 신청한 입찰 리스트 Dto")
public class BodyShopBidPageResDto {
    @Schema(description = "입찰 식별자", example = "1")
    private final Long id;

    @Schema(description = "견적건 주인", example = "caumuch1234")
    private final String client;

    @Schema(description = "손상 부위", example = "범퍼")
    private final String damageArea;

    @Schema(description = "입찰 상태", example = "WAITING")
    private final String bidStatus;

    @Schema(description = "입찰 신청 일")
    private final LocalDateTime createDate;

    @Builder
    public BodyShopBidPageResDto(Long id, String client, String damageArea, String bidStatus, LocalDateTime createDate) {
        this.id = id;
        this.client = client;
        this.damageArea = damageArea;
        this.bidStatus = bidStatus;
        this.createDate = createDate;
    }
}
