package com.carumuch.capstone.domain.auth.filter;

import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import io.jsonwebtoken.IncorrectClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        /* OAuth2 재로그인 무한 루프 방지*/
        String requestUri = request.getRequestURI();
        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = tokenProvider.resolveAccessToken(request);

        try {
            if (accessToken != null && tokenProvider.validateAccessToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthenticationByAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IncorrectClaimException e) {
            SecurityContextHolder.clearContext();
            log.debug("잘못된 토큰 입니다.");
            response.sendError(403);
        } catch (UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
            log.debug("회원을 찾을 수 없습니다..");
            response.sendError(403);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.equals("/login") || requestURI.equals("/users/signup") || requestURI.equals("/reissue");
    }
}
