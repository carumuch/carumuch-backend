package com.carumuch.capstone.vehicle.domain;

import com.carumuch.capstone.bodyshop.domain.Bid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "estimate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id")
    private Long id; // 견적 번호

    @Column(name = "description", length = 300)
    private String description; // 상세 설명

    @Column(name = "damage_area", length = 100)
    private String damageArea; // 손상 부위

    @Column(name = "preferred_repair_location", length = 100)
    private String preferredRepairLocation; // 수리 희망 지역

    @Column(name = "pickup_required")
    private boolean pickupRequired;

    @Column(name = "image_name", length = 255)
    private String imageName; // 사진 이름

    @Column(name = "image_path", length = 500)
    private String imagePath; // 사진 경로

    @OneToMany(mappedBy = "estimate", cascade = PERSIST)
    private List<Bid> bids = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Builder
    public Estimate(String description, String damageArea, String preferredRepairLocation, boolean pickupRequired, String imageName, String imagePath) {
        this.description = description;
        this.damageArea = damageArea;
        this.preferredRepairLocation = preferredRepairLocation;
        this.pickupRequired = pickupRequired;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    /* 견적 내용 수정 */
    public void update(String description, String damageArea, String preferredRepairLocation, boolean pickupRequired) {
        this.description = description;
        this.damageArea = damageArea;
        this.preferredRepairLocation = preferredRepairLocation;
        this.pickupRequired = pickupRequired;
    }
}
