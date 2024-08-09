package com.carumuch.capstone.vehicle.domain;

import com.carumuch.capstone.bodyshop.domain.Bid;
import com.carumuch.capstone.global.auditing.BaseCreateByEntity;
import com.carumuch.capstone.global.validation.ValidationGroups;
import com.carumuch.capstone.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Estimate extends BaseCreateByEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estimate_id")
    private Long id; // 견적 번호

    @Column(name = "description", length = 300)
    private String description; // 상세 설명

    @Column(name = "damage_area", length = 100)
    private String damageArea; // 손상 부위

    @Column(name = "preferred_repair_sido", length = 100)
    private String preferredRepairSido; // 수리 희망 지역

    @Column(name = "preferred_repair_sigungu", length = 100)
    private String preferredRepairSigungu; // 수리 희망 지역

    @Column(name = "ai_estimated_repair_cost")
    private Integer aiEstimatedRepairCost; // AI를 통한 예상 수리 가격

    @Column(name = "is_ai_estimate")
    private boolean isAIEstimate; // AI 를 통한 견적서 확인 여부

    @Column(name = "is_pickup_required")
    private boolean isPickupRequired; // 픽업 희망 여부

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

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Estimate(String description,
                    String damageArea,
                    String preferredRepairSido,
                    String preferredRepairSigungu,
                    Integer aiEstimatedRepairCost,
                    boolean isAIEstimate,
                    boolean isPickupRequired,
                    String imageName,
                    String imagePath) {
        this.description = description;
        this.damageArea = damageArea;
        this.preferredRepairSido = preferredRepairSido;
        this.preferredRepairSigungu = preferredRepairSigungu;
        this.aiEstimatedRepairCost = aiEstimatedRepairCost;
        this.isAIEstimate = isAIEstimate;
        this.isPickupRequired = isPickupRequired;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    /* 연관 관계 메서드 */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicle.getEstimates().add(this);
    }
    public void setUser(User user) {
        this.user = user;
        user.getEstimates().add(this);
    }

    /* 견적 내용 수정 */
    public void update(String description,
                       String damageArea,
                       String preferredRepairSido,
                       String preferredRepairSigungu,
                       boolean isPickupRequired) {
        this.description = description;
        this.damageArea = damageArea;
        this.preferredRepairSido = preferredRepairSido;
        this.preferredRepairSigungu = preferredRepairSigungu;
        this.isPickupRequired = isPickupRequired;
    }

    /* 기본 견적서 생성 */
    // TODO: 이미지 관련 파라미터 추가
    public static Estimate createBasicEstimate(String description,
                                               String damageArea,
                                               String preferredRepairSido,
                                               String preferredRepairSigungu,
                                               boolean isPickupRequired,
                                               User user,
                                               Vehicle vehicle) {
        Estimate estimate = Estimate.builder()
                .description(description)
                .damageArea(damageArea)
                .preferredRepairSido(preferredRepairSido)
                .preferredRepairSigungu(preferredRepairSigungu)
                .isAIEstimate(false)
                .isPickupRequired(isPickupRequired)
                .build();

        estimate.setUser(user);
        estimate.setVehicle(vehicle);

        return estimate;
    }

    /* AI 견적서 생성*/
    // TODO: 이미지 관련 파라미터 추가
    public static Estimate createAIEstimate(String description,
                                            String damageArea,
                                            String preferredRepairSido,
                                            String preferredRepairSigungu,
                                            Integer aiEstimatedRepairCost,
                                            boolean isPickupRequired,
                                            User user,
                                            Vehicle vehicle) {
        Estimate estimate = Estimate.builder()
                .description(description)
                .damageArea(damageArea)
                .preferredRepairSido(preferredRepairSido)
                .preferredRepairSigungu(preferredRepairSigungu)
                .aiEstimatedRepairCost(aiEstimatedRepairCost)
                .isAIEstimate(true)
                .isPickupRequired(isPickupRequired)
                .build();

        estimate.setUser(user);
        estimate.setVehicle(vehicle);

        return estimate;
    }
}
