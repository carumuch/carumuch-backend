package com.carumuch.capstone.bidding.domain;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.estimate.domain.Estimate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

import org.springframework.http.HttpStatus;

@Entity
@Table(name = "bid")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid extends AggregateRoot<Bid> {

    @Column(name = "cost")
    private int cost;

    @Column(name = "repair_method")
    private String repairMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BidStatus bidStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "body_shop_id")
    private BodyShop bodyShop;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;

    private Bid(int cost, String repairMethod, BodyShop bodyShop, Estimate estimate) {
        this.cost = cost;
        this.repairMethod = repairMethod;
        this.bidStatus = BidStatus.WAITING;
        this.bodyShop = bodyShop;
        this.estimate = estimate;
    }

    public static Bid apply(int cost, String repairMethod, BodyShop bodyShop, Estimate estimate) {
        if (estimate.isClosed()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "입찰이 종료된 견적서 입니다.");
        }
        return new Bid(cost, repairMethod, bodyShop, estimate);
    }

    public void update(int cost, String repairMethod) {
        if (!this.bidStatus.equals(BidStatus.WAITING)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "낙찰 혹은 거절된 입찰건입니다.");
        }
        this.cost = cost;
        this.repairMethod = repairMethod;
    }

    public void accept() {
        if (!this.bidStatus.equals(BidStatus.WAITING)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 낙찰 혹은 거절된 입찰건입니다.");
        }
        this.bidStatus = BidStatus.ACCEPTED;
        this.bodyShop.increaseAcceptCount();
        this.estimate.closeBidding();
    }

    public void reject() {
        if (this.bidStatus.equals(BidStatus.ACCEPTED)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 낙찰되었습니다.");
        }
        this.bidStatus = BidStatus.REJECTED;
    }
}
