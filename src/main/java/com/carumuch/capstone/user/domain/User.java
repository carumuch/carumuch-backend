package com.carumuch.capstone.user.domain;

import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.global.auditing.BaseTimeEntity;
import com.carumuch.capstone.user.domain.type.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

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

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public User(String loginId, String password, String email, String name, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
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
}
