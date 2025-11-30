package com.carumuch.capstone.support.fixture;

import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.model.type.Role;
import lombok.Getter;

@Getter
public enum UserFixture {
    USER_FIXTURE_1("testLoginId", "password1234@", "test@gmail.com", "홍길동", Role.USER),
    USER_FIXTURE_2("testLoginId2", "password1234@", "test2@gmail.com", "존도", Role.USER),
    USER_FIXTURE_3("testLoginId3", "password1234@", "test3@gmail.com", "제인도", Role.USER);

    private final String loginId;
    private final String password;
    private final String email;
    private final String name;
    private final Role role;

    UserFixture(String loginId, String password, String email, String name, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public User createUser() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .name(name)
                .role(role)
                .build();
    }
}
