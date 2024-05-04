package com.carumuch.capstone.user.controller;

import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.user.dto.JoinReqDto;
import com.carumuch.capstone.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;

    /**
     * CREATE: 회원 가입
     */
    @PostMapping
    public ResponseEntity<?> join(@Validated(ValidationSequence.class) @RequestBody JoinReqDto joinReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, userService.join(joinReqDto)));
    }
}
