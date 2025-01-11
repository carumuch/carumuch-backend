package com.carumuch.capstone.domain.auth.dto;

import com.carumuch.capstone.domain.user.model.type.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {

    private final String loginId;
    private final String name;
    private final Role role;

    @Builder
    public UserDto(String loginId, String name, Role role) {
        this.loginId = loginId;
        this.name = name;
        this.role = role;
    }
}
