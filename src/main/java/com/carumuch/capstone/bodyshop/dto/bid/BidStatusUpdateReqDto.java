package com.carumuch.capstone.bodyshop.dto.bid;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidStatusUpdateReqDto {

    @NotBlank(message = "변경할 상태가 입력되지 않았습니다..", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(ACCEPTED|REJECTED)$",
            message = "입찰 상태는 ACCEPTED 또는 REJECTED만 가능합니다.", groups = ValidationGroups.PatternGroup.class)
    @Schema(description = "입찰 상태 변경입니다. ACCEPTED, REJECTED",
            example = "ACCEPTED")
    private String status;

    public BidStatusUpdateReqDto(String status) {
        this.status = status;
    }
}
