package com.carumuch.capstone.auth.service;

import com.carumuch.capstone.auth.dto.CustomUserDetails;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.domain.UserLegacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserLegacyRepository userLegacyRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String loginId) {
        User findUser = userLegacyRepository.findLoginUserByLoginId(loginId);
        if(findUser != null){
            CustomUserDetails userDetails = new CustomUserDetails(findUser);
            return  userDetails;
        }
        return null;
    }
}
