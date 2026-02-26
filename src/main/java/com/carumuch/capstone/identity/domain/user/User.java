package com.carumuch.capstone.identity.domain.user;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.domain.AggregateRoot;
import com.carumuch.capstone.community.domain.Board;
import com.carumuch.capstone.community.domain.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AggregateRoot<User> {

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

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "body_shop_id")
	private BodyShop bodyShop;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Comment> comments = new ArrayList<>();

	@Builder
    public User(String loginId, String password, String email, String name, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
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

	public void assignBodyShop(BodyShop bodyShop) {
		this.bodyShop = bodyShop;
	}

	public boolean isMechanic() {
		return bodyShop != null;
	}
}
