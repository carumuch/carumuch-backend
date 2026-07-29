package com.carumuch.capstone.bidding.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BidStatus {
    WAITING("WAITING", "입찰 대기 상태"),
    ACCEPTED("ACCEPTED", "입찰 수락 상태"),
    REJECTED("REJECTED", "입찰 거절 상태"),
    CANCELED("CANCELED", "입찰 취소 상태");

    private final String key;
    private final String title;
}

