package com.carumuch.capstone.bodyshop.domain;

import com.carumuch.capstone.bodyshop.domain.type.BidStatus;
import com.carumuch.capstone.global.auditing.BaseCreateByEntity;
import com.carumuch.capstone.vehicle.domain.Estimate;
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
public class Bid extends BaseCreateByEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long id;

    @Column(name = "cost")
    private int cost; // 수리비

    @Column(name = "repair_method")
    private String repairMethod;// 수리 방법

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BidStatus bidStatus; // 입찰 상태

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "body_shop_id")
    private BodyShop bodyShop;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;

    @Builder
    public Bid(int cost, String repairMethod, BidStatus bidStatus, BodyShop bodyShop, Estimate estimate) {
        this.cost = cost;
        this.repairMethod = repairMethod;
        this.bidStatus = bidStatus;
        this.bodyShop = bodyShop;
        this.estimate = estimate;
    }

    /* 연관 관계 메서드 */
    public void setBodyShop(BodyShop bodyShop) {
        this.bodyShop = bodyShop;
        bodyShop.getBids().add(this);
    }
    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
        estimate.getBids().add(this);
    }

    /* 입찰 신청 */
    public void apply(BodyShop bodyShop, Estimate estimate) {
        setBodyShop(bodyShop);
        setEstimate(estimate);
    }

    /* 입찰 상태 변경 */
    public void update(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }
}
