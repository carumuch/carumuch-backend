package com.carumuch.capstone.bodyshop.domain;

import java.util.Objects;

import com.carumuch.capstone.common.domain.AccessPolicy;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.common.domain.Location;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "body_shop")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BodyShop extends AggregateRoot<BodyShop> implements AccessPolicy {

    @Column(name = "name", length = 100)
    private String name;

    @Embedded
    private Location location;

    @Column(name = "description", length = 200)
    private String description;

	@Embedded
	@AttributeOverride(
		name = "value",
		column = @Column(name = "phone_number", length = 15, nullable = false)
	)
    private PhoneNumber phoneNumber;

    @Column(name = "link", length = 200)
    private String link;

    @Column(name = "accept_count")
    private int acceptCount;

    @Column(name = "pickup_available")
    private boolean pickupAvailable;

	@Column(name = "manager_user_id", nullable = false, updatable = false)
	private Long managerUserId;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public BodyShop(
		String name,
		Location location,
		String description,
		String link,
		PhoneNumber phoneNumber,
		boolean pickupAvailable,
		Long managerUserId
	) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.phoneNumber = phoneNumber;
        this.pickupAvailable = pickupAvailable;
		this.managerUserId = managerUserId;
    }

    public void update(
		String name,
		Location location,
		String description,
		String link,
		PhoneNumber phoneNumber,
		boolean pickupAvailability
	) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.phoneNumber = phoneNumber;
        this.pickupAvailable = pickupAvailability;
    }

    public void increaseAcceptCount() {
        this.acceptCount += 1;
    }

	@Override
	public boolean canAccess(Long userId) {
		return Objects.equals(this.managerUserId, userId);
	}
}
