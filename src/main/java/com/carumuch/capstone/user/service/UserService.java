package com.carumuch.capstone.user.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.domain.type.Role;
import com.carumuch.capstone.user.dto.JoinReqDto;
import com.carumuch.capstone.user.dto.UpdatePasswordReqDto;
import com.carumuch.capstone.user.dto.UpdateReqDto;
import com.carumuch.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * Update: 회원 수정
     */
    @Transactional
    public Long update(UpdateReqDto updateReqDto) {
        checkEmailDuplicate(updateReqDto.getEmail()); // 이메일 중복 확인
        User user = userRepository.findLonginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        user.update(
                updateReqDto.getName(),
                updateReqDto.getEmail());
        return user.getId();
    }

    /**
     * Update: 비밀번호 수정
     */
    @Transactional
    public Long updatePassword(UpdatePasswordReqDto updatePasswordReqDto) {
        User user = userRepository.findLonginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        String encodeNewPassword = bCryptPasswordEncoder.encode(updatePasswordReqDto.getNewPassword());
        // 비밀번호가 일치하지 않을 때
        if (!bCryptPasswordEncoder.matches(updatePasswordReqDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        user.updatePassword(encodeNewPassword);
        return user.getId();
    }
}
