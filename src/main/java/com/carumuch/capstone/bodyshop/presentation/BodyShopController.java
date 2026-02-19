package com.carumuch.capstone.bodyshop.presentation;

import com.carumuch.capstone.bodyshop.presentation.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.presentation.dto.BodyShopUpdateReqDto;
import com.carumuch.capstone.bodyshop.application.BodyShopService;
import com.carumuch.capstone.common.legacy.dto.ResponseDto;
import com.carumuch.capstone.common.legacy.validation.ValidationSequence;
import com.carumuch.capstone.identity.domain.user.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/body-shops")
@RequiredArgsConstructor
public class BodyShopController {
    private final BodyShopService bodyShopService;

    @PostMapping
    public ResponseEntity<?> register(@Validated(ValidationSequence.class) @RequestBody BodyShopRegistrationReqDto bodyShopRegistrationReqDto, User user) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.register(bodyShopRegistrationReqDto, user.getId())));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchKeyword(@RequestParam(defaultValue = "1") int page, @RequestParam String keyword) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.searchKeyword(page,keyword)));
    }

    @PutMapping("/{bodyShopId}")
    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody BodyShopUpdateReqDto bodyShopUpdateReqDto,
                                    @PathVariable Long bodyShopId, User user) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.update(bodyShopId,bodyShopUpdateReqDto, user.getId())));
    }

    @GetMapping("/{bodyShopId}")
    public ResponseEntity<?> detail(@PathVariable Long bodyShopId) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.findOne(bodyShopId)));
    }
}
