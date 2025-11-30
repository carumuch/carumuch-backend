package com.carumuch.capstone.support.security;

import com.carumuch.capstone.domain.auth.dto.CustomUserDetails;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.support.annotation.WithMockCustom;
import com.carumuch.capstone.support.fixture.UserFixture;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class MockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustom> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustom customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User mockUser = UserFixture.USER_FIXTURE_1.createUser();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(mockUser), null, List.of(new SimpleGrantedAuthority(customUser.role())));
        context.setAuthentication(authentication);

        return context;
    }
}
