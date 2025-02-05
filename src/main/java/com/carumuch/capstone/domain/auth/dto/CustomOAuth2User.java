package com.carumuch.capstone.domain.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserDto OAuth2UserDto;

    public CustomOAuth2User(OAuth2UserDto OAuth2UserDto) {
        this.OAuth2UserDto = OAuth2UserDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return OAuth2UserDto.getRole().getKey();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return OAuth2UserDto.getName();
    }

    public String getLoginId() {
        return OAuth2UserDto.getLoginId();
    }
}
