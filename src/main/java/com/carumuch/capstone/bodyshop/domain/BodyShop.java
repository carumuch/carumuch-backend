package com.carumuch.capstone.bodyshop.domain;

import static jakarta.persistence.CascadeType.*;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.identity.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "body_shop")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BodyShop extends AggregateRoot<BodyShop> {

    @Column(name = "name", length = 100)
    private String name;

    @Embedded
    private Location location;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "link", length = 200)
    private String link;

    @Column(name = "accept_count")
    private int acceptCount;

    @Column(name = "pickup_availability")
    private boolean pickupAvailability;

    @OneToMany(mappedBy = "bodyShop", cascade = ALL)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "bodyShop", cascade = PERSIST)
    private List<Bid> bids = new ArrayList<>();

    public BodyShop(String name, Location location, String description, String link, String phoneNumber, boolean pickupAvailability) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.phoneNumber = phoneNumber;
        this.pickupAvailability = pickupAvailability;
    }

    public void update(String name, Location location, String description, String link, String phoneNumber, boolean pickupAvailability) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.phoneNumber = phoneNumber;
        this.pickupAvailability = pickupAvailability;
    }

	// TODO: 원자적 연산이 아니라 동시성 문제가 우려됨, 수정 필요
    public void acceptCount() {
        this.acceptCount += 1;
    }
}
