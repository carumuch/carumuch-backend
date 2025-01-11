package com.carumuch.capstone.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.carumuch.capstone.global.validation.ValidationGroups.*;

@Getter
@NoArgsConstructor
@Schema(name = "JoinReqDto: 회원가입 요청 Dto")
public class UserJoinReqDto {

    @NotBlank(message = "아이디가 입력되지 않았습니다.", groups = NotBlankGroup.class)
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.",
            groups = PatternGroup.class)
    @Schema(description = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리입니다",
            example = "carumuch1234")
    private String loginId;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.", groups = NotBlankGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$",
            message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 포함 해야 합니다.",
            groups = PatternGroup.class)
    @Schema(description = "비밀번호는 8~16자리 수 입니다. 영문 대소문자, 숫자, 특수문자를 포함합니다.",
            example = "helloworld1234@")
    private String password;

    @NotBlank(message = "이메일이 입력되지 않았습니다.", groups = NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "이메일 형식에 맞지 않습니다.",
            groups = PatternGroup.class)
    @Schema(description = "이메일 형식이여야 합니다.",
            example = "carumuch@gmail.com")
    private String email;

    @NotBlank(message = "이름이 입력되지 않았습니다.", groups = NotBlankGroup.class)
    @Schema(description = "사용자 이름입니다.",
            example = "차박고")
    private String name;

    @Builder
    public UserJoinReqDto(String loginId, String password, String email, String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
    }
}
