package com.carumuch.capstone.bodyshop.controller;

import com.carumuch.capstone.bodyshop.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopUpdateReqDto;
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

    /**
     * Create: 기존 공업사로 등록
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<?> join(@PathVariable Long id) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.join(id)));
    }

    /**
     * UPDATE: 공업사 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody BodyShopUpdateReqDto bodyShopUpdateReqDto,
                                    @PathVariable Long id) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.update(id,bodyShopUpdateReqDto)));
    }

    /**
     * UPDATE: 다른 공업사로 변경
     */
    @PatchMapping("/{id}/transfer")
    public ResponseEntity<?> transfer(@PathVariable  Long id) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, bodyShopService.transfer(id)));
    }

    /**
     * SELECT: 공업사 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, bodyShopService.findOne(id)));
    }
}