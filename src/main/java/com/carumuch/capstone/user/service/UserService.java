package com.carumuch.capstone.user.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.domain.type.Role;
import com.carumuch.capstone.user.dto.JoinReqDto;
import com.carumuch.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public Long join(JoinReqDto joinReqDto) {
        checkLoginIdDuplicate(joinReqDto.getLoginId()); // 아이디 중복 확인
        checkEmailDuplicate(joinReqDto.getEmail()); // 이메일 중복 확인
        return userRepository.save(User.builder()
                .loginId(joinReqDto.getLoginId())
                .password(bCryptPasswordEncoder.encode(joinReqDto.getPassword()))
                .email(joinReqDto.getEmail())
                .name(joinReqDto.getName())
                .role(Role.USER)
                .build()).getId();
    }
}
