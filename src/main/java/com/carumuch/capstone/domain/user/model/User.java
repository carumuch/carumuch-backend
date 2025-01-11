package com.carumuch.capstone.domain.user.model;

import com.carumuch.capstone.domain.board.model.Board;
import com.carumuch.capstone.domain.bodyshop.model.BodyShop;
import com.carumuch.capstone.domain.comment.model.Comment;
import com.carumuch.capstone.global.auditing.BaseTimeEntity;
import com.carumuch.capstone.domain.user.model.type.Role;
import com.carumuch.capstone.domain.estimate.model.Estimate;
import com.carumuch.capstone.domain.vehicle.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login_id", unique = true)
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "is_mechanic")
    private boolean isMechanic;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "body_shop_id")
    private BodyShop bodyShop;

    @OneToMany(mappedBy = "user", cascade = {PERSIST, REMOVE})
    private List<Estimate> estimates = new ArrayList<>();

    @OneToOne(fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Builder
    public User(String loginId, String password, String email, String name, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.isMechanic = false; // 가입시 기본 회원
    }

    /* OAuth2 사용자 정보 업데이트 */
    public void updateOAuth2(String name,String email) {
        this.name = name;
        this.email = email;
    }

    /* 사용자 정보 수정 */
    public void update(String name,String email) {
        this.name = name;
        this.email = email;
    }

    /* 사용자 비밀번호 수정 */
    public void updatePassword(String password) {
        this.password = password;
    }

    /* body shop 사용자 등록 */
    public void setBodyShop(BodyShop bodyShop) {
        this.bodyShop = bodyShop;
        bodyShop.getUsers().add(this);
    }

    /* 공업사 직원으로 변경 */
    public void registerMechanic() {
        this.isMechanic = true;
    }

    /* 연관관계 설정을 위한 메서드*/
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
