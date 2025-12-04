package com.carumuch.capstone.vehicle.domain;

import static jakarta.persistence.FetchType.*;

import com.carumuch.capstone.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "damage_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageReport extends BaseEntity<DamageReport> {

	@Column(name = "description", length = 300)
	private String description;

	@Column(name = "preferred_repair_sido", length = 100)
	private String preferredRepairSido;

	@Column(name = "preferred_repair_sigungu", length = 100)
	private String preferredRepairSigungu;

	@Column(name = "is_pickup_required")
	private boolean isPickupRequired;

	@Column(name = "image_path", length = 500)
	private String imagePath;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;

	public DamageReport(String description, String preferredRepairSido, String preferredRepairSigungu, boolean isPickupRequired, String imagePath, Vehicle vehicle) {
		this.description = description;
		this.preferredRepairSido = preferredRepairSido;
		this.preferredRepairSigungu = preferredRepairSigungu;
		this.isPickupRequired = isPickupRequired;
		this.imagePath = imagePath;
		this.vehicle = vehicle;
	}
}
