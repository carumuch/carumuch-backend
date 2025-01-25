package com.carumuch.capstone.domain.user.service;

import com.carumuch.capstone.domain.user.dto.UserInfoResDto;
import com.carumuch.capstone.domain.user.dto.UserJoinReqDto;
import com.carumuch.capstone.domain.user.dto.UserUpdatePasswordReqDto;
import com.carumuch.capstone.domain.user.dto.UserUpdateReqDto;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.model.type.Role;
import com.carumuch.capstone.domain.user.repository.UserRepository;
import com.carumuch.capstone.global.exception.ErrorCode;
import com.carumuch.capstone.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * READ: 회원 정보
     */
    public UserInfoResDto info(String loginId) {
        User user = userRepository.findLoginUserByLoginId(loginId);
        return UserInfoResDto.builder()
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * 아이디 중복 확인
     **/
    public void checkLoginIdDuplicate(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGINID);
        }
    }

    /**
     * 이메일 중복 확인
     */
    public void checkEmailDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    /**
     * Create: 회원 가입
     */
    @Transactional
    public Long join(UserJoinReqDto userJoinReqDto) {
        checkLoginIdDuplicate(userJoinReqDto.getLoginId()); // 아이디 중복 확인
        checkEmailDuplicate(userJoinReqDto.getEmail()); // 이메일 중복 확인
        Long userId = userRepository.save(User.builder()
                .loginId(userJoinReqDto.getLoginId())
                .password(bCryptPasswordEncoder.encode(userJoinReqDto.getPassword()))
                .email(userJoinReqDto.getEmail())
                .name(userJoinReqDto.getName())
                .role(Role.USER)
                .build()).getId();
        log.info(userJoinReqDto.getName() + " : " + "join" + "(" + new Date() + ")");
        return userId;
    }

    /**
     * Update: 회원 수정
     */
    @Transactional
    public Long update(UserUpdateReqDto userUpdateReqDto) {
        checkEmailDuplicate(userUpdateReqDto.getEmail()); // 이메일 중복 확인
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        user.update(
                userUpdateReqDto.getName(),
                userUpdateReqDto.getEmail());
        return user.getId();
    }

    /**
     * Update: 비밀번호 수정
     */
    @Transactional
    public Long updatePassword(UserUpdatePasswordReqDto userUpdatePasswordReqDto) {
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        String encodeNewPassword = bCryptPasswordEncoder.encode(userUpdatePasswordReqDto.getNewPassword());
        // 비밀번호가 일치하지 않을 때
        if (!bCryptPasswordEncoder.matches(userUpdatePasswordReqDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        user.updatePassword(encodeNewPassword);
        return user.getId();
    }
}
