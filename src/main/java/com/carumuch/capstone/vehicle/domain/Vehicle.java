package com.carumuch.capstone.vehicle.domain;

import com.carumuch.capstone.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "vehicle")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;

    @Column(name = "license_number")
    private int licenseNumber; // 차량 번호

    @Column(name = "type")
    private String type; // 차량 유형 -> 법인/리스, 개인

    @Column(name = "brand")
    private String brand; // 차량 브랜드

    @Column(name = "model_year")
    private int modelYear; // 차량 연형

    @Column(name = "model_name")
    private String modelName;// 차량 모델

    @Column(name = "owner_name")
    private String ownerName; // 차량 실 소유자 명

    @OneToMany(mappedBy = "vehicle", cascade = ALL)
    private List<Estimate> estimates = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Vehicle(int licenseNumber, String type, String brand, int modelYear, String modelName, String ownerName) {
        this.licenseNumber = licenseNumber;
        this.type = type;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.ownerName = ownerName;
    }

    /* 연관 관계 메서드 */
    public void setUser(User user) {
        this.user = user;
        user.getVehicles().add(this);
    }

    /* 차량 정보 수정 */
    public void update(int licenseNumber, String type, String brand, int modelYear, String modelName, String ownerName) {
        this.licenseNumber = licenseNumber;
        this.type = type;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.ownerName = ownerName;
    }
}
