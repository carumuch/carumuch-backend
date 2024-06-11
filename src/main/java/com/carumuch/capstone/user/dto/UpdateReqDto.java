package com.carumuch.capstone.user.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "UpdateReqDto: 회원 수정 요청 Dto")
public class UpdateReqDto {

    @NotBlank(message = "이메일이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "이메일 형식에 맞지 않습니다.",
            groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "이메일 형식이여야 합니다.",
            example = "carbackgo@naver.com")
    private String email;

    @NotBlank(message = "이름이 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Schema(description = "사용자 이름입니다.",
            example = "카우머치")
    private String name;

    @Builder
    public UpdateReqDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
