package com.carumuch.capstone.bidding.domain;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "bid")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid extends AggregateRoot<Bid> {

    @Column(name = "cost")
    private int cost; // 수리비

    @Column(name = "repair_method")
    private String repairMethod;// 수리 방법

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BidStatus bidStatus; // 입찰 상태

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "body_shop_id")
    private BodyShop bodyShop;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;

    @Builder
    public Bid(int cost, String repairMethod, BidStatus bidStatus, BodyShop bodyShop, Estimate estimate) {
        this.cost = cost;
        this.repairMethod = repairMethod;
        this.bidStatus = bidStatus;
        this.bodyShop = bodyShop;
		apply(bodyShop, estimate);
    }

    /* 입찰 신청 */
    public void apply(BodyShop bodyShop, Estimate estimate) {
        this.bodyShop = bodyShop;
        this.estimate = estimate;
    }

    /* 입찰 정보 수정 */
    public void update(int cost, String repairMethod) {
        this.cost = cost;
        this.repairMethod = repairMethod;
    }

    /* 입찰 상태 변경 */
    public void updateStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }
}
