package com.carumuch.capstone.estimate.domain;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.damage.domain.report.DamageReport;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "estimate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate extends AggregateRoot<Estimate> {

	@Column(name = "ai_estimated_repair_cost")
	private Integer repairCost;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "estimate_repair_parts", joinColumns = @JoinColumn(name = "estimate_id"))
	@Column(name = "part_name")
	private Set<String> repairParts = new HashSet<>();

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

    public Estimate(Integer repairCost, Set<String> repairParts, EstimateStatus estimateStatus, DamageReport damageReport) {
        this.repairCost = repairCost;
		this.repairParts = repairParts;
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
