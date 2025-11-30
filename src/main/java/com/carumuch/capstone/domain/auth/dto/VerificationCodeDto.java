package com.carumuch.capstone.domain.auth.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "VerificationCodeDto: 인증번호 인증 Dto")
public class VerificationCodeDto {

    @NotBlank(message = "아이디가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.",
            groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리입니다",
            example = "carumuch1234")
    private String loginId;

    @NotBlank(message = "인증번호가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 4자리 숫자여야 합니다.",
            groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "인증번호는 6자리 숫자 입니다.",
            example = "123456")
    private String code;

    @Builder
    public VerificationCodeDto(String loginId, String code) {
        this.loginId = loginId;
        this.code = code;
    }
}
