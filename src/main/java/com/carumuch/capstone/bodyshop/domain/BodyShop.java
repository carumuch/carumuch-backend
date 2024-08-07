package com.carumuch.capstone.bodyshop.domain;

import com.carumuch.capstone.global.auditing.BaseCreateByEntity;
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
public class BodyShop extends BaseCreateByEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "body_shop_id")
    private Long id;

    @Column(name = "name", length = 100)
    private String name; // 샵 이름

    @Embedded
    private Location location; // 샵 지역

    @Column(name = "description", length = 200)
    private String description; // 샵 설명

    @Column(name = "phone_number", length = 15)
    private String phoneNumber; // 전화번호

    @Column(name = "link", length = 200)
    private String link; // 홈페이지

    @Column(name = "accept_count")
    private int acceptCount; // 수리 채결 count

    @Column(name = "pickup_availability")
    private boolean pickupAvailability; // 픽 업 가능 여부

    @OneToMany(mappedBy = "bodyShop", cascade = PERSIST)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "bodyShop", cascade = PERSIST)
    private List<Bid> bids = new ArrayList<>();

    @Builder
    public BodyShop(String name, Location location, String description, String link, String phoneNumber, User user, int acceptCount, boolean pickupAvailability) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.phoneNumber = phoneNumber;
        this.acceptCount = acceptCount;
        this.pickupAvailability = pickupAvailability;
        user.setBodyShop(this);
    }

    /* 공업사 정보 수정 */
    public void update(String name, Location location, String description, String link, String phoneNumber, boolean pickupAvailability) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.link = link;
        this.phoneNumber = phoneNumber;
        this.pickupAvailability = pickupAvailability;
    }

    /* 입찰 횟수 증가 */
    public void acceptCount() {
        this.acceptCount += 1;
    }
}
