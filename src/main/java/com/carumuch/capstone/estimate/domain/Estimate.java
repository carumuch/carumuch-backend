package com.carumuch.capstone.estimate.domain;

import com.carumuch.capstone.common.domain.AccessPolicy;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.damage.domain.report.DamageReport;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.HttpStatus;

@Entity
@Table(
	name = "estimate",
	indexes = {
		@Index(
			name = "idx_estimate_status_created_cost",
			columnList = "status, create_date DESC, ai_estimated_repair_cost, damage_report_id"
		)
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate extends AggregateRoot<Estimate> implements AccessPolicy {

	@Column(name = "ai_estimated_repair_cost", nullable = false)
	private Integer repairCost;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "estimate_repair_parts", joinColumns = @JoinColumn(name = "estimate_id"))
	@Column(name = "part_name")
	private Set<String> repairParts = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private EstimateStatus estimateStatus;

    @Column(name = "applicant_count")
    private int applicantCount;

	@Column(name = "image_path", length = 500, nullable = false)
	private String imagePath;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "damage_report_id", unique = true, nullable = false)
	private DamageReport damageReport;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Version
	@Column(name = "version", nullable = false)
	private Long version;

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

	public void closeBidding() {
		validateStatus();
		this.estimateStatus = EstimateStatus.CLOSED;
	}

	private void validateStatus() {
		if (this.estimateStatus == EstimateStatus.CLOSED) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이미 매칭된 견적서 입니다.");
		}
	}

	public void increaseApplicantCount() {
		this.applicantCount++;
	}

	public void decreaseApplicantCount() {
		this.applicantCount--;
	}

	public boolean isOpen() {
		return this.estimateStatus == EstimateStatus.OPEN;
	}

	public boolean isClosed() {
		return this.estimateStatus == EstimateStatus.CLOSED;
	}

	public boolean isPrivate() {
		return this.estimateStatus == EstimateStatus.PRIVATE;
	}

	@Override
	public boolean canAccess(Long userId) {
		return Objects.equals(this.userId, userId);
	}
}
