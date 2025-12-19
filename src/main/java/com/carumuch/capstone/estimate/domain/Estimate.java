package com.carumuch.capstone.estimate.domain;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;
import com.carumuch.capstone.identity.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "estimate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate extends AggregateRoot<Estimate> {

	// TODO: 손상 부위들을 세팅합니다.

    @Column(name = "damage_area", length = 100)
    private String damageArea; // TODO 레거시 유지를 위해 남겨둡니다. 이후 삭제 조치 해야합니다.

    @Column(name = "ai_estimated_repair_cost")
    private Integer aiEstimatedRepairCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EstimateStatus estimateStatus;

    @Column(name = "applicant_count")
    private int applicantCount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "damage_report_id", unique = true)
	private DamageReport damageReport;

    @OneToMany(mappedBy = "estimate", cascade = ALL)
    private List<Bid> bids = new ArrayList<>();

    public Estimate(Integer aiEstimatedRepairCost, EstimateStatus estimateStatus, DamageReport damageReport) {
        this.aiEstimatedRepairCost = aiEstimatedRepairCost;
        this.estimateStatus = estimateStatus;
		this.damageReport = damageReport;
    }

    public void update(EstimateStatus estimateStatus) {
        this.estimateStatus = estimateStatus;
    }

    // TODO: 원자적 연산이 아니라 동시성 문제가 우려됨, 수정 필요
    public void increaseApplicant() {
        this.applicantCount += 1;
    }
}
