package com.carumuch.capstone.bodyshop.dto.bid;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "BodyShopBidDetailResDto: 공업사 측 입찰 상세 확인 Dto")
public class BodyShopBidDetailResDto {

    @Schema(description = "입찰 식별자", example = "1")
    private final Long id;

    @Schema(description = "공업사 측 입찰 제안 수리비", example = "300000")
    private final int cost;

    @Schema(description = "공업사 측 입찰 수리 방법", example = "앞 범퍼를 탈거 하여 수리 후, 도색 작업을 거쳐 다시 차량에 장착할 예정입니다.")
    private final String repairMethod;

    @Schema(description = "입찰 상태", example = "WAITING")
    private final String bidStatus;

    @Schema(description = "입찰 신청 일")
    private final LocalDateTime createDate;

    @Builder
    public BodyShopBidDetailResDto(Long id, int cost, String repairMethod, String bidStatus, LocalDateTime createDate) {
        this.id = id;
        this.cost = cost;
        this.repairMethod = repairMethod;
        this.bidStatus = bidStatus;
        this.createDate = createDate;
    }
}
