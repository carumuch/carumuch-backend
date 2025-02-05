package com.carumuch.capstone.domain.auth.service;

import com.carumuch.capstone.domain.auth.dto.VerificationCodeDto;
import com.carumuch.capstone.domain.auth.dto.VerificationLoginIdDto;
import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.carumuch.capstone.global.exception.ErrorCode;
import com.carumuch.capstone.global.exception.CustomException;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.repository.UserRepository;
import com.carumuch.capstone.global.service.MailService;
import com.carumuch.capstone.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final MailService mailService;
    private final UserRepository userRepository;


    /**
     * 1. 비밀번호 찾기: 인증번호 전송
     */
    public void sendVerificationCode(VerificationLoginIdDto verificationLoginIdDto) {
        User user = userRepository.findByLoginId(verificationLoginIdDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String email = user.getEmail();
        String name = user.getName();
        String code = mailService.createCode();

        if (redisService.getCode(email) != null) {
            redisService.deleteCode(email);
        }

        long codeValidityInMilliseconds = 120 * 1000;

        redisService.saveCode(email, code, codeValidityInMilliseconds);
        mailService.sendVerificationCodeMail(name, email, code);
        log.info(user.getLoginId() + " : " + "sendCodeMail" + "(" + new Date() + ")");
    }

    /**
     * 2. 비밀번호 찾기: 인증번호 검증
     */
    public String verifyCode(VerificationCodeDto verificationCodeDto) {
        User user = userRepository.findLoginUserByLoginId(verificationCodeDto.getLoginId());
        String email = user.getEmail();
        String code = redisService.getCode(email);

        if (!code.equals(verificationCodeDto.getCode())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return tokenProvider.createTemporaryToken(email, new Date());
    }

    /**
     * 3. 비밀번호 찾기: 새 비밀번호 업데이트
     */
    @Transactional
    public void resetPassword(String email, String encodePassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updatePassword(encodePassword);
    }

    /**
     * 아이디 찾기
     */
    public void findLoginId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String loginId = user.getLoginId();

        // 난독화 진행 -> 2,3번 째 아이디 "*"로 변경
        String obfuscationLoginId = loginId.charAt(0) + "**" + loginId.substring(3);

        mailService.sendFindLoginIdMail(user.getName(), user.getEmail(), obfuscationLoginId);
        log.info(user.getLoginId() + " : " + "sendFindLoginIdMail" + "(" + new Date() + ")");
    }
}
