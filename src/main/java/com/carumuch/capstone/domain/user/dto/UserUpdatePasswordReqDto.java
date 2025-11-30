package com.carumuch.capstone.domain.user.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "UpdatePasswordReqDto: 비밀번호 수정 요청 Dto")
public class UserUpdatePasswordReqDto {

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$",
            message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 포함 해야 합니다.",
            groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "비밀번호는 8~16자리 수 입니다. 영문 대소문자, 숫자, 특수문자를 포함합니다.",
            example = "helloworld1234@")
    private String password;

    @NotBlank(message = "변경 할 비밀번호가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$",
            message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 포함 해야 합니다.",
            groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "비밀번호는 8~16자리 수 입니다. 영문 대소문자, 숫자, 특수문자를 포함합니다.",
            example = "carbackgo1212!")
    private String newPassword;

    @Builder
    public UserUpdatePasswordReqDto(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }
}
