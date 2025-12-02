package com.carumuch.capstone.user.domain;

import com.carumuch.capstone.community.domain.Board;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.community.domain.Comment;
import com.carumuch.capstone.common.domain.BaseEntity;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.vehicle.domain.Vehicle;
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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity<User> {

	@Column(name = "login_id", length = 30, unique = true)
	private String loginId;

	@Column(name = "password", length = 200, nullable = false)
	private String password;

	@Column(name = "email", length = 30, unique = true)
	private String email;

	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", length = 20, nullable = false)
	private Role role;

    @Column(name = "is_mechanic")
    private boolean isMechanic;

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
        this.isMechanic = false;
		registerEvent(new UserRegisteredEvent(this));

    }

	public void updateInfo(String name) {
		this.name = name;
	}

	public void updatePassword(String encodedNewPassword) {
		this.password = encodedNewPassword;
	}

	public void withdraw() {
		registerEvent(new UserWithdrawnEvent(this.loginId));
	}

	//== 레거시 도메인 로직==// TODO: 사용되지 않을 때 삭제합니다.
    public void setBodyShop(BodyShop bodyShop) {
        this.bodyShop = bodyShop;
        bodyShop.getUsers().add(this);
    }

    public void registerMechanic() {
        this.isMechanic = true;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
