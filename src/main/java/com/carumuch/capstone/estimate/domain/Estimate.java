package com.carumuch.capstone.estimate.domain;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.common.domain.AccessPolicy;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.damage.domain.report.DamageReport;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

import org.springframework.http.HttpStatus;

@Entity
@Table(name = "estimate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate extends AggregateRoot<Estimate> implements AccessPolicy {

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

	@Column(name = "image_path", length = 500)
	private String imagePath;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "damage_report_id", unique = true)
	private DamageReport damageReport;

    @OneToMany(mappedBy = "estimate", cascade = ALL)
    private List<Bid> bids = new ArrayList<>();

	@Column(name = "user_id")
	private Long userId;

    public Estimate(
		Integer repairCost,
		Set<String> repairParts,
		EstimateStatus estimateStatus,
		String imagePath,
		DamageReport damageReport
	) {
        this.repairCost = repairCost;
		this.repairParts = repairParts;
        this.estimateStatus = estimateStatus;
		this.imagePath = imagePath;
		this.damageReport = damageReport;
		this.userId = damageReport.getUserId();
    }

    public void changeStatus(EstimateStatus estimateStatus) {
		if (estimateStatus == EstimateStatus.CLOSED) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "CLOSED 상태로는 변경할 수 없습니다.");
		}
		validateStatus();
        this.estimateStatus = estimateStatus;
    }

	private void validateStatus() {
		if (this.estimateStatus == EstimateStatus.CLOSED) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이미 매칭된 견적서 입니다.");
		}
	}

	@Override
	public boolean canAccess(Long userId) {
		return Objects.equals(this.userId, userId);
	}

	// TODO: 원자적 연산이 아니라 동시성 문제가 우려됨, 수정 필요
    public void increaseApplicant() {
        this.applicantCount += 1;
    }
}
