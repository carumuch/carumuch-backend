package com.carumuch.capstone.auth.service;

import com.carumuch.capstone.auth.dto.*;
import com.carumuch.capstone.auth.dto.oauth2.GoogleResponse;
import com.carumuch.capstone.auth.dto.oauth2.KakaoResponse;
import com.carumuch.capstone.auth.dto.oauth2.NaverResponse;
import com.carumuch.capstone.auth.dto.oauth2.OAuth2Response;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.domain.type.Role;
import com.carumuch.capstone.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.carumuch.capstone.global.common.ErrorCode.*;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final long ACCESS_TOKEN_EXPIRATION = 3600 * 1000;

    public CustomOAuth2UserService(UserRepository userRepository, RedisService redisService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        /* 회원 탈퇴 토큰 추출 */
        String oauth2AccessToken = userRequest.getAccessToken().getTokenValue();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }
        String loginId = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        if (userRepository.existsByEmail(oAuth2Response.getEmail())) {
            throw new OAuth2AuthenticationException("중복 소셜 회원 가입");
        }
        if (!userRepository.existsByLoginId(loginId)) {
            /* 유저 저장 */
            userRepository.save(User.builder()
                    .loginId(loginId)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role(Role.USER)
                    .build());

            /*레디스 소셜 로그인 토큰 저장*/
            redisService.setValuesWithTimeout("AT(oauth2):" + loginId , oauth2AccessToken, ACCESS_TOKEN_EXPIRATION);

            /* 유저 정보 전달 */
            return new CustomOAuth2User(UserDto.builder()
                    .loginId(loginId)
                    .name(oAuth2Response.getName())
                    .role(Role.USER)
                    .build());
        }
        else {
            User user = userRepository.findOAuth2UserByLoginId(loginId);
            user.updateOAuth2(oAuth2Response.getName(), oAuth2Response.getEmail());

            /* oauth2 토큰 중복 방지 */
            if (redisService.getValues("AT(oauth2):" + loginId) != null) {
                redisService.deleteValues("AT(oauth2):" + loginId);
            }
            /* 레디스 토큰 정보 */
            redisService.setValuesWithTimeout("AT(oauth2):" + loginId ,oauth2AccessToken, ACCESS_TOKEN_EXPIRATION);

            return new CustomOAuth2User(UserDto.builder()
                    .loginId(user.getLoginId())
                    .name(oAuth2Response.getName()) // 받아온 값
                    .role(user.getRole())
                    .build());
        }
    }
}
