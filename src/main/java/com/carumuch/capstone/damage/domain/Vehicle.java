package com.carumuch.capstone.damage.domain;

import com.carumuch.capstone.common.domain.BaseEntity;
import com.carumuch.capstone.estimate.domain.estimate.Estimate;
import com.carumuch.capstone.identity.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(
	name = "vehicle",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_vehicle_user_id",
			columnNames = "user_id"),
		@UniqueConstraint(
			name = "uk_vehicle_license_number",
			columnNames = "license_number")
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle extends BaseEntity<Vehicle> {

	@Embedded
	@AttributeOverride(
		name = "value",
		column = @Column(name = "license_number")
	)
    private LicenseNumber licenseNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "ownership_type")
	private VehicleOwnershipType ownershipType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model_year")
    private Integer modelYear;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "owner_name")
    private String ownerName;

    @OneToMany(mappedBy = "vehicle", cascade = ALL)
    private List<Estimate> estimates = new ArrayList<>();

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
    private User user;

    public Vehicle(
		String licenseNumber,
		VehicleOwnershipType ownershipType,
		String brand,
		Integer modelYear,
		String modelName,
		String ownerName,
		User user
	) {
        this.licenseNumber = new LicenseNumber(licenseNumber);
        this.ownershipType = ownershipType;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.ownerName = ownerName;
        this.user = user;
    }

	public void update(
		String licenseNumber,
		VehicleOwnershipType ownershipType,
		String brand,
		Integer modelYear,
		String modelName,
		String ownerName
	) {
		this.licenseNumber = new LicenseNumber(licenseNumber);
		this.ownershipType = ownershipType;
		this.brand = brand;
		this.modelYear = modelYear;
		this.modelName = modelName;
		this.ownerName = ownerName;
	}
}