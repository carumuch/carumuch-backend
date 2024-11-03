package com.carumuch.capstone.auth.service;

import com.carumuch.capstone.auth.dto.CustomUserDetails;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String loginId) {
        User findUser = userRepository.findLoginUserByLoginId(loginId);
        if(findUser != null){
            CustomUserDetails userDetails = new CustomUserDetails(findUser);
            return  userDetails;
        }
        return null;
    }
}
