package com.carumuch.capstone.user.controller;

import com.carumuch.capstone.auth.service.AuthService;
import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.global.validation.ValidationSequence;
import com.carumuch.capstone.user.dto.UserJoinReqDto;
import com.carumuch.capstone.user.dto.UserUpdatePasswordReqDto;
import com.carumuch.capstone.user.dto.UserUpdateReqDto;
import com.carumuch.capstone.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;
    private final AuthService authService;

    /**
     * READ: 회원 정보
     */
    @GetMapping
    public ResponseEntity<?> info() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, userService.info(loginId)));
    }

    /**
     * CREATE: 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> join(@Validated(ValidationSequence.class) @RequestBody UserJoinReqDto userJoinReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, userService.join(userJoinReqDto)));
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

    /**
     * Delete: 회원 탈퇴
     */
    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request) {
        authService.delete(request.getHeader("Authorization"));
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", null)
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(false)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ResponseDto.success(OK,null));
    }

    /**
     * Update: 회원 수정
     */
    @PutMapping
    public ResponseEntity<?> update(@Validated(ValidationSequence.class) @RequestBody UserUpdateReqDto userUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, userService.update(userUpdateReqDto)));
    }

    /**
     * Update: 비밀번호 수정
     */
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Validated(ValidationSequence.class) @RequestBody UserUpdatePasswordReqDto userUpdatePasswordReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, userService.updatePassword(userUpdatePasswordReqDto)));
    }
}
