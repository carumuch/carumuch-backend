package com.carumuch.capstone.bodyshop.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "BidPageResDto: 입찰 리스트 Dto")
public class BidPageResDto {
    @Schema(description = "입찰 식별자", example = "1")
    private final Long id;

    @Schema(description = "수리비", example = "50000")
    private final int cost;

    @Schema(description = "입찰 상태", example = "WAITING")
    private final String bidStatus;

    @Schema(description = "입찰 신청 일")
    private final LocalDateTime createDate;

    @Builder
    public BidPageResDto(Long id, int cost, String bidStatus, LocalDateTime createDate) {
        this.id = id;
        this.cost = cost;
        this.bidStatus = bidStatus;
        this.createDate = createDate;
    }
}
