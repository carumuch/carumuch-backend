package com.carumuch.capstone.domain.auth.service;

import com.carumuch.capstone.domain.auth.dto.OAuth2UserDto;
import com.carumuch.capstone.domain.auth.dto.oauth2.GoogleResponse;
import com.carumuch.capstone.domain.auth.dto.oauth2.KakaoResponse;
import com.carumuch.capstone.domain.auth.dto.oauth2.NaverResponse;
import com.carumuch.capstone.domain.auth.dto.oauth2.OAuth2Response;
import com.carumuch.capstone.domain.auth.dto.CustomOAuth2User;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.model.type.Role;
import com.carumuch.capstone.domain.user.repository.UserRepository;
import com.carumuch.capstone.global.service.RedisService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


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

        if (!userRepository.existsByLoginId(loginId)) {
            if (userRepository.existsByEmail(oAuth2Response.getEmail())) {
                throw new OAuth2AuthenticationException("중복 소셜 회원 가입");
            }
            userRepository.save(User.builder()
                    .loginId(loginId)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role(Role.USER)
                    .build());

            redisService.saveOauth2AccessToken(loginId , oauth2AccessToken, ACCESS_TOKEN_EXPIRATION);

            return new CustomOAuth2User(OAuth2UserDto.builder()
                    .loginId(loginId)
                    .name(oAuth2Response.getName())
                    .role(Role.USER)
                    .build());
        }
        else {
            User user = userRepository.findOAuth2UserByLoginId(loginId);
            user.updateOAuth2(oAuth2Response.getName(), oAuth2Response.getEmail());

            if (redisService.getOauth2AccessToken(loginId) != null) {
                redisService.deleteOauth2AccessToken(loginId);
            }
            redisService.saveOauth2AccessToken(loginId ,oauth2AccessToken, ACCESS_TOKEN_EXPIRATION);

            return new CustomOAuth2User(OAuth2UserDto.builder()
                    .loginId(user.getLoginId())
                    .name(oAuth2Response.getName()) // 받아온 값
                    .role(user.getRole())
                    .build());
        }
    }
}
