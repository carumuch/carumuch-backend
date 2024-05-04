package com.carumuch.capstone.user.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.domain.type.Role;
import com.carumuch.capstone.user.dto.JoinReqDto;
import com.carumuch.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 아이디 중복 확인
     **/
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    /**
     * 이메일 중복 확인
     */
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Create: 회원 가입
     */
    @Transactional
    public Long join(JoinReqDto joinReqDto) {
        if (checkLoginIdDuplicate(joinReqDto.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGINID);
        }
        if (checkEmailDuplicate(joinReqDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        return userRepository.save(User.builder()
                .loginId(joinReqDto.getLoginId())
                .password(joinReqDto.getPassword())
                .email(joinReqDto.getEmail())
                .name(joinReqDto.getName())
                .role(Role.ADMIN)
                .build()).getId();
    }
}
