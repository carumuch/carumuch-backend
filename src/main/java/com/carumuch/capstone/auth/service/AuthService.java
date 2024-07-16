package com.carumuch.capstone.auth.service;

import com.carumuch.capstone.auth.dto.ResetPasswordDto;
import com.carumuch.capstone.auth.dto.TokenDto;
import com.carumuch.capstone.auth.dto.VerificationCodeDto;
import com.carumuch.capstone.auth.dto.VerificationLoginIdDto;
import com.carumuch.capstone.auth.jwt.JwtTokenProvider;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.domain.type.Role;
import com.carumuch.capstone.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final String SERVER = "Server";

    private final OAuth2UnlinkService oAuth2UnlinkService;
    private final MailService mailService;


    /**
     * 로그아웃: Access Token 무효화
     */
    @Transactional
    public void logout(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(requestAccessToken);

        /* Redis에 저장되어 있는 Refresh Token 삭제 */
        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis != null) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal);
        }

        /* 소셜 로그인 유저 일 경우 oauth2 access 토큰 삭제 */
        if (redisService.getValues("AT(oauth2):" + principal) != null) {
            redisService.deleteValues("AT(oauth2):" + principal);
        }

        /* Redis에 로그아웃 처리한 Access Token 저장 */
        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
        redisService.setValuesWithTimeout(requestAccessToken,
                "logout",
                expiration);
        log.info(principal + " : " + "logout" + "(" + new Date() + ")");
    }

    /**
     * DELETE
     */
    @Transactional
    public void delete(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(requestAccessToken);

        /* Redis에 저장되어 있는 Refresh Token 삭제 */
        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis != null) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal);
        }
        /* Redis에 회원탈퇴 처리한 Access Token 저장 */
        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
        redisService.setValuesWithTimeout(requestAccessToken,
                "delete",
                expiration);
        /* DB에 저장 되어 있는 회원 삭제 */
        userRepository.deleteByLoginId(principal);
        if (principal.startsWith("kakao") || principal.startsWith("google") || principal.startsWith("naver")) {
            oAuth2UnlinkService.unlink(principal);
        }
        /* oauth2 access 토큰 삭제 */
        if (redisService.getValues("AT(oauth2):" + principal) != null) {
            redisService.deleteValues("AT(oauth2):" + principal);
        }
        log.info(principal + " : " + "delete" + "(" + new Date() + ")");
    }

    /* Access Token 만료일자만 초과한 유효한 토큰인지 검사 */
    public boolean validate(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken); // true = 재발급
    }

    /**
     *  토큰 재발급: validate 메서드가 true 반환할 때만 사용 -> Access Token, Refresh Token 재발급
     */
    @Transactional
    public TokenDto reissue(String requestAccessTokenInHeader, String requestRefreshToken) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
        String principal = authentication.getName();

        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis == null) { // Redis에 저장되어 있는 Refresh Token이 없을 경우
            return null; // -> 재로그인 요청
        }

        /* 요청된 Refresh Token 의 유효성 검사 & Redis에 저장되어 있는 Refresh Token과 같은지 비교 */
        if(!jwtTokenProvider.validateRefreshToken(requestRefreshToken) || !refreshTokenInRedis.equals(requestRefreshToken)) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal); // 탈취 가능성 -> 삭제
            return null; // -> 재로그인 요청
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);

        /* 토큰 재발급 및 Redis 업데이트 */
        redisService.deleteValues("RT(" + SERVER + "):" + principal); // 기존 Refresh Token 삭제
        TokenDto tokenDto = jwtTokenProvider.createToken(principal, authorities);
        saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());
        return tokenDto;
    }

    /**
     * 토큰 발급
      */
    @Transactional
    public TokenDto generateToken(String provider, String loginId, String authorities) {
        /* Refresh Token이 이미 있을 경우 삭제*/
        if(redisService.getValues("RT(" + provider + "):" + loginId) != null) {
            redisService.deleteValues("RT(" + provider + "):" + loginId);
        }

        /* Access, Refresh Token 생성 및 Redis에 RefreshToken 저장 */
        TokenDto tokenDto = jwtTokenProvider.createToken(loginId, authorities);
        saveRefreshToken(provider, loginId, tokenDto.getRefreshToken());
        return tokenDto;
    }

    /**
     * Refresh Token 을 Redis 에 저장
     */
    @Transactional
    public void saveRefreshToken(String provider, String principal, String refreshToken) {
        redisService.setValuesWithTimeout("RT(" + provider + "):" + principal, // key
                refreshToken, // value
                jwtTokenProvider.getTokenExpirationTime(refreshToken)); // timeout(milliseconds)
    }

    /**
     * 권한 이름 가져오기
     */
    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * Access Token principal 추출
     */
    public String getPrincipal(String requestAccessToken) {
        return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
    }

    /**
     * "Bearer {Access Token}" 에서 AccessToken 추출
     */
    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
            return requestAccessTokenInHeader.substring(7);
        }
        return null;
    }

    /**
     * 1. 비밀번호 찾기: 인증번호 전송
     */
    public void sendVerificationCode(VerificationLoginIdDto verificationLoginIdDto) {
        User user = userRepository.findByLoginId(verificationLoginIdDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        /* 메일 요소 */
        String email = user.getEmail();
        String name = user.getName();
        String code = mailService.createCode();

        if (redisService.getValues("CODE(" + SERVER + "):" + email) != null) {
            redisService.deleteValues("CODE(" + SERVER + "):" + email);
        }

        /* 인증코드 유호 시간 2분*/
        long codeValidityInMilliseconds = 120 * 1000;

        /* 레디스 저장 */
        redisService.setValuesWithTimeout("CODE(" + SERVER + "):" + email, code, codeValidityInMilliseconds);
        /* 이메일 발송 */
        mailService.sendVerificationCodeMail(name, email, code);
        log.info(user.getLoginId() + " : " + "sendCodeMail" + "(" + new Date() + ")");
    }

    /**
     * 2. 비밀번호 찾기: 인증번호 검증
     */
    @Transactional
    public String verifyCode(VerificationCodeDto verificationCodeDto) {
        User user = userRepository.findLoginUserByLoginId(verificationCodeDto.getLoginId());
        String email = user.getEmail();
        String code = redisService.getValues("CODE(" + SERVER + "):" + email);

        if (!code.equals(verificationCodeDto.getCode())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return jwtTokenProvider.createTemporaryToken(email);
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
