package com.carumuch.capstone.domain.auth.service;

import com.carumuch.capstone.domain.auth.dto.CustomUserDetails;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.repository.UserRepository;
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
