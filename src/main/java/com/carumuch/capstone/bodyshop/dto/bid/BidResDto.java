package com.carumuch.capstone.bodyshop.dto.bid;

import com.carumuch.capstone.bodyshop.domain.Bid;
import com.carumuch.capstone.bodyshop.dto.bodyShop.BodyShopInfoResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class BidResDto {

    @Schema(description = "입찰 식별자", example = "1")
    private final Long id;

    @Schema(description = "입찰 가격", example = "50000")
    private final int cost;

    @Schema(description = "공업사 측 수리 방법",
            example = "앞 범퍼를 탈거 하여 수리 후, 도색 작업을 거쳐 다시 차량에 장착할 예정입니다.")
    private final String repairMethod;

    @Schema(description = "입찰 상태", example = "ACCEPTED")
    private final String bidStatus;

    @Schema(description = "신청 공업사 정보")
    private final List<BodyShopInfoResDto> bodyShop;

    public BidResDto(Bid bid) {
        this.id = bid.getId();
        this.cost = bid.getCost();
        this.repairMethod = bid.getRepairMethod();
        this.bidStatus = bid.getBidStatus().getKey();

        this.bodyShop = List.of(
                BodyShopInfoResDto.builder()
                        .id(bid.getBodyShop().getId())
                        .name(bid.getBodyShop().getName())
                        .location(bid.getBodyShop().getLocation())
                        .description(bid.getBodyShop().getDescription())
                        .phoneNumber(String.valueOf(bid.getBodyShop().getPhoneNumber()))
                        .acceptCount(bid.getBodyShop().getAcceptCount())
                        .link(bid.getBodyShop().getLink())
                        .pickupAvailability(bid.getBodyShop().isPickupAvailability())
                .build());
    }
}
