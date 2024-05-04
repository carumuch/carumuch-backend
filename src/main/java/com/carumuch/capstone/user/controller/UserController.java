package com.carumuch.capstone.user.controller;

import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationGroups;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.user.dto.JoinReqDto;
import com.carumuch.capstone.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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

    /**
     * 아이디 중복 확인
     */
    @GetMapping("/check-login-id/{loginId}")
    public ResponseEntity<?> checkLoginId(@PathVariable String loginId) {
        userService.checkLoginIdDuplicate(loginId);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, true));
    }

    /**
     * 이메일 중복 확인
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
        userService.checkEmailDuplicate(email);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, true));
    }
}
