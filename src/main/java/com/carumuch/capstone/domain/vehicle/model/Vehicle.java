package com.carumuch.capstone.domain.vehicle.model;

import com.carumuch.capstone.domain.estimate.model.Estimate;
import com.carumuch.capstone.global.base.BaseCreateByEntity;
import com.carumuch.capstone.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "vehicle")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle extends BaseCreateByEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;

    @Column(name = "license_number", unique = true)
    private String licenseNumber; // 차량 번호

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

    @OneToOne(fetch = LAZY, mappedBy = "vehicle")
    private User user;

    @Builder
    public Vehicle(String licenseNumber, String type, String brand, int modelYear, String modelName, String ownerName, User user) {
        this.licenseNumber = licenseNumber;
        this.type = type;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.ownerName = ownerName;
        setUser(user);
    }

    public void setUser(User user) {
        this.user = user;
        user.setVehicle(this);
    }

    /* 차량 정보 수정 */
    public void update(String licenseNumber, String type, String brand, Integer modelYear, String modelName, String ownerName) {
        this.licenseNumber = licenseNumber;
        this.type = type;
        this.brand = brand;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.ownerName = ownerName;
    }
}