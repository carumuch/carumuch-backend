package com.carumuch.capstone.bodyshop.controller;

import com.carumuch.capstone.bodyshop.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.service.BodyShopService;
import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/body-shops")
@RequiredArgsConstructor
public class BodyShopController implements BodyShopControllerDocs{
    private final BodyShopService bodyShopService;

    /**
     * CREATE: 신규 공업사 등록
     */
    @PostMapping
    public ResponseEntity<?> register(@Validated(ValidationSequence.class) @RequestBody BodyShopRegistrationReqDto bodyShopRegistrationReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.register(bodyShopRegistrationReqDto)));
    }

    /**
     * SELECT: 공업사 키워드 검색
     */
    @GetMapping("/{id}/search")
    public ResponseEntity<?> searchKeyword(@PathVariable int id, @RequestParam String keyword) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.searchKeyword(id,keyword)));
    }
}
