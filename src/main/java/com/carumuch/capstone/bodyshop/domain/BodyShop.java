package com.carumuch.capstone.bodyshop.domain;

import com.carumuch.capstone.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "body_shop")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BodyShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "body_shop_id")
    private Long id;

    @Column(name = "name", length = 100)
    private String name; // 샵 이름

    @Column(name = "location", length = 100)
    private String location; // 샵 지역

    @Column(name = "description", length = 100)
    private String description; // 샵 설명

    @Column(name = "accept_count")
    private int acceptCount; // 수리 채결 count

    @Column(name = "pickup_availability")
    private boolean pickupAvailability; // 픽 업 가능 여부

    @OneToMany(mappedBy = "bodyShop", cascade = PERSIST)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "bodyShop", cascade = PERSIST)
    private List<Bid> bids = new ArrayList<>();

    @Builder
    public BodyShop(String name, String location, String description, int acceptCount, boolean pickupAvailability) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.acceptCount = acceptCount;
        this.pickupAvailability = pickupAvailability;
    }

    /* 공업사 정보 수정 */
    public void update(String name, String location, String description, boolean pickupAvailability) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.pickupAvailability = pickupAvailability;
    }

    /* 입찰 횟수 증가 */
    public void acceptCount() {
        this.acceptCount += 1;
    }
}
