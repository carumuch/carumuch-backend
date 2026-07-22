package com.carumuch.capstone.bidding.domain;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.domain.AccessPolicy;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.estimate.domain.Estimate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

import java.util.Objects;

import org.springframework.http.HttpStatus;

@Entity
@Table(name = "bid")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid extends AggregateRoot<Bid> implements AccessPolicy {

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
        validateCost(cost);
        return new Bid(cost, repairMethod, bodyShop, estimate);
    }

    public void accept() {
        validateBidWaiting();
        this.bidStatus = BidStatus.ACCEPTED;
    }

    public void reject() {
        validateBidWaiting();
        this.bidStatus = BidStatus.REJECTED;
    }

    public void cancel() {
        validateBidWaiting();
        this.bidStatus = BidStatus.CANCELED;
    }

    public void update(int cost, String repairMethod) {
        validateBidWaiting();
        validateCost(cost);
        this.cost = cost;
        this.repairMethod = repairMethod;
    }

    private void validateBidWaiting() {
        if (!this.bidStatus.equals(BidStatus.WAITING)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "대기 중인 입찰만 처리할 수 있습니다.");
        }
    }

    private static void validateCost(int cost) {
        if (cost <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "입찰 금액은 0원보다 커야 합니다.");
        }
    }

    private boolean isBidder(Long userId) {
        return Objects.equals(this.bodyShop.getManagerUserId(), userId);
    }

    private boolean isEstimateRequester(Long userId) {
        return Objects.equals(this.estimate.getUserId(), userId);
    }

    public void validateBidder(Long userId) {
        if (!isBidder(userId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "입찰한 공업사만 처리할 수 있습니다.");
        }
    }

    public void validateEstimateRequester(Long userId) {
        if (!isEstimateRequester(userId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "견적서 주인만 처리할 수 있습니다.");
        }
    }

    @Override
    public boolean canAccess(Long userId) {
        return isBidder(userId) || isEstimateRequester(userId);
    }
}
