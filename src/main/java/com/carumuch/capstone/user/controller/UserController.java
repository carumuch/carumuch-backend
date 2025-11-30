package com.carumuch.capstone.user.controller;

import com.carumuch.capstone.user.dto.UserJoinReqDto;
import com.carumuch.capstone.user.dto.UserUpdatePasswordReqDto;
import com.carumuch.capstone.user.dto.UserUpdateReqDto;
import com.carumuch.capstone.common.legacy.dto.ResponseDto;
import com.carumuch.capstone.common.legacy.utils.CookieUtil;
import com.carumuch.capstone.common.legacy.validation.ValidationSequence;
import com.carumuch.capstone.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.carumuch.capstone.common.legacy.constants.TokenConstant.REFRESH_EXPIRATION_DELETE;
import static com.carumuch.capstone.common.legacy.constants.TokenConstant.REFRESH_TOKEN_COOKIE_NAME;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        userService.delete(request);
        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.SET_COOKIE, CookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, null, REFRESH_EXPIRATION_DELETE).toString())
                .body(null);
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
