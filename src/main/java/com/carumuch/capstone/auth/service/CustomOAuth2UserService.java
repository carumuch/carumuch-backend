package com.carumuch.capstone.auth.service;

import com.carumuch.capstone.auth.dto.*;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.domain.type.Role;
import com.carumuch.capstone.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

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

            /* 유저 저장 */
            userRepository.save(User.builder()
                    .loginId(loginId)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role(Role.USER)
                    .build());

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

            return new CustomOAuth2User(UserDto.builder()
                    .loginId(user.getLoginId())
                    .name(oAuth2Response.getName()) // 받아온 값
                    .role(user.getRole())
                    .build());
        }
    }
}
