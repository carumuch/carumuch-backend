package com.carumuch.capstone.bidding.presentation.dto.response;

import java.time.LocalDateTime;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.presentation.dto.LocationResponse;

public record BidListResponse(
    Long id,
    int cost,
    String bidStatus,
    LocalDateTime createDate,
    BodyShopInfo BodyShopInfo
) {
    public BidListResponse(Bid bid) {
        this(
            bid.getId(),
            bid.getCost(),
            bid.getBidStatus().toString(),
            bid.getCreateDate(),
            new BodyShopInfo(bid.getBodyShop())
        );
    }

    private record BodyShopInfo(
        Long id,
        String name,
        String link,
        String phoneNumber,
        int acceptCount,
        LocationResponse location
    ) {
        private BodyShopInfo(BodyShop bodyShop) {
            this(
                bodyShop.getId(),
                bodyShop.getName(),
                bodyShop.getLink(),
                bodyShop.getPhoneNumber().getValue(),
                bodyShop.getAcceptCount(),
                LocationResponse.from(bodyShop.getLocation())
            );
        }
    }
}
