package com.carumuch.capstone.bidding.presentation.dto.response;

import java.time.LocalDateTime;

import com.carumuch.capstone.bidding.domain.Bid;

public record BidListResponse(
    Long id,
    int cost,
    String bidStatus,
    LocalDateTime createDate
) {
    public BidListResponse(Bid bid) {
        this(
            bid.getId(),
            bid.getCost(),
            bid.getBidStatus().toString(),
            bid.getCreateDate()
        );
    }
}
