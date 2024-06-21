package com.carumuch.capstone.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "InfoResDto: 회원 정보 응답 Dto")
public class UserInfoResDto {

    @Schema(description = "사용자 아이디", example = "carumuch1234")
    private final String loginId;
    @Schema(description = "사용자 이메일", example = "carumuch@gmail.com")
    private final String email;
    @Schema(description = "사용자 이름", example = "차박고")
    private final String name;

    @Builder
    public UserInfoResDto(String loginId, String email, String name) {
        this.loginId = loginId;
        this.email = email;
        this.name = name;
    }
}
