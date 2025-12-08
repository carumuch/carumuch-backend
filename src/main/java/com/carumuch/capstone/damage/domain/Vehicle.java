package com.carumuch.capstone.damage.domain;

import com.carumuch.capstone.common.domain.BaseEntity;
import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.estimate.domain.estimate.Estimate;
import com.carumuch.capstone.identity.domain.user.User;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

import org.springframework.http.HttpStatus;

@Entity
@Table(
	name = "vehicle",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk_vehicle_user_id",
			columnNames = "user_id"
		)
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle extends BaseEntity<Vehicle> {
	private static final String LICENSE_NUMBER_PATTERN = "^(?:\\d{2}|\\d{3})[가-힣]\\d{4}$";

	@Column(name = "license_number", unique = true)
    private String licenseNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "ownership_type")
	private VehicleOwnershipType ownershipType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model_year")
    private int modelYear;

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
		int modelYear,
		String modelName,
		String ownerName,
		User user
	) {
		validateLicenseNumber(licenseNumber);
        this.licenseNumber = licenseNumber;
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
		validateLicenseNumber(licenseNumber);
		this.licenseNumber = licenseNumber;
		this.ownershipType = ownershipType;
		this.brand = brand;
		this.modelYear = modelYear;
		this.modelName = modelName;
		this.ownerName = ownerName;
	}

	private void validateLicenseNumber(String licenseNumber) {
		if (licenseNumber == null || !licenseNumber.matches(LICENSE_NUMBER_PATTERN)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "올바른 차량 번호 형식이 아닙니다.");
		}
	}
}