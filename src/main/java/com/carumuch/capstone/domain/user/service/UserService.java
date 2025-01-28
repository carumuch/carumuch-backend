package com.carumuch.capstone.domain.user.service;

import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.carumuch.capstone.domain.auth.service.OAuth2UnlinkService;
import com.carumuch.capstone.domain.user.dto.UserInfoResDto;
import com.carumuch.capstone.domain.user.dto.UserJoinReqDto;
import com.carumuch.capstone.domain.user.dto.UserUpdatePasswordReqDto;
import com.carumuch.capstone.domain.user.dto.UserUpdateReqDto;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.model.type.Role;
import com.carumuch.capstone.domain.user.repository.UserRepository;
import com.carumuch.capstone.global.exception.ErrorCode;
import com.carumuch.capstone.global.exception.CustomException;
import com.carumuch.capstone.global.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.carumuch.capstone.global.constants.TokenConstant.AUTHORITIES_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final OAuth2UnlinkService oAuth2UnlinkService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void delete(HttpServletRequest request) {
        String accessToken = tokenProvider.resolveAccessToken(request);
        Claims claimsByAccessToken = tokenProvider.getClaimsByAccessToken(accessToken);

        String loginId = claimsByAccessToken.getSubject();
        String role = claimsByAccessToken.get(AUTHORITIES_KEY).toString();

        tokenProvider.invalidateRefreshToken(role, loginId);
        userRepository.deleteByLoginId(loginId);
        deleteOauth2AccessToken(loginId);

        log.info("{}-{}: delete ({})", loginId, role, new Date());
    }

    private void deleteOauth2AccessToken(String loginId) {
        if (loginId.startsWith("kakao") || loginId.startsWith("google") || loginId.startsWith("naver")) {
            oAuth2UnlinkService.unlink(loginId);
        }
        if (redisService.getOauth2AccessToken(loginId) != null) {
            redisService.deleteOauth2AccessToken(loginId);
        }
    }

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
