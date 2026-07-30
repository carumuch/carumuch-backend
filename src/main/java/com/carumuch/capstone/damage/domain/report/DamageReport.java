package com.carumuch.capstone.damage.domain.report;

import static jakarta.persistence.FetchType.*;

import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.damage.domain.vehicle.Vehicle;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "damage_report",
	indexes = {
		@Index(
			name = "idx_dr_region_pickup_vehicle",
			columnList = "preferred_repair_sido, preferred_repair_sigungu, is_pickup_required, vehicle_id"
		)
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageReport extends AggregateRoot<DamageReport> {
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "sido", column = @Column(name = "preferred_repair_sido", length = 100)),
			@AttributeOverride(name = "sigungu", column = @Column(name = "preferred_repair_sigungu", length = 100))}
	)
	private RepairRegion preferredRepairRegion;

	@Column(name = "description", length = 300)
	private String description;

	@Column(name = "is_pickup_required")
	private boolean isPickupRequired;

	@Column(name = "image_path", length = 500)
	private String imagePath;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20, nullable = false)
	private DamageReportStatus status;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "vehicle_id", nullable = false)
	private Vehicle vehicle;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	public DamageReport(String description, RepairRegion preferredRepairRegion, boolean isPickupRequired, String imagePath, Vehicle vehicle, Long userId) {
		this.preferredRepairRegion = preferredRepairRegion;
		this.description = description;
		this.isPickupRequired = isPickupRequired;
		this.imagePath = imagePath;
		this.status = DamageReportStatus.REGISTERED;
		this.vehicle = vehicle;
		this.userId = userId;
	}

	public void update(String description, RepairRegion preferredRepairRegion, boolean isPickupRequired) {
		this.description = description;
		this.preferredRepairRegion = preferredRepairRegion;
		this.isPickupRequired = isPickupRequired;
	}
}
