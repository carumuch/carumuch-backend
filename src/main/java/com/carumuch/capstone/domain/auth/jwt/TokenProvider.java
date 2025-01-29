package com.carumuch.capstone.domain.auth.jwt;


import com.carumuch.capstone.global.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.carumuch.capstone.global.constants.TokenConstant.*;


@Slf4j
@Component
public class TokenProvider {

    private final RedisService redisService;

    private final UserDetailsService userDetailsService;
    private final SecretKey accessTokenSigningKey;
    private final SecretKey refreshTokenSigningKey;
    private final long accessTokenExpirationSeconds;
    private final long refreshTokenExpirationSeconds;

    public TokenProvider(
            RedisService redisService,
            UserDetailsService userDetailsService,
            @Value("${spring.jwt.access-secret-key}") String accessTokenSecret,
            @Value("${spring.jwt.refresh-secret-key}") String refreshTokenSecret,
            @Value("${spring.jwt.access-token-validity-in-seconds}") Long accessTokenExpirationSeconds,
            @Value("${spring.jwt.refresh-token-validity-in-seconds}") Long refreshTokenExpirationSeconds) {
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        this.accessTokenSigningKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshTokenSigningKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
        this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
    }

    public String createAccessToken(String loginId, String roles, Date now) {
        long accessTokenExpirationMilliseconds = accessTokenExpirationSeconds * 1000;
        Date accessExpiredTime = new Date(now.getTime() + accessTokenExpirationMilliseconds);

        return Jwts.builder()
                .subject(loginId)
                .claim(AUTHORITIES_KEY, roles)
                .issuedAt(now)
                .expiration(accessExpiredTime)
                .signWith(accessTokenSigningKey)
                .compact();
    }

    public String createRefreshToken(String loginId, String roles, Date now) {
        long refreshTokenExpirationMilliseconds = refreshTokenExpirationSeconds * 1000;
        Date refreshExpiredTime = new Date(now.getTime() + refreshTokenExpirationMilliseconds);

        String refreshToken = Jwts.builder()
                .subject(loginId)
                .issuedAt(now)
                .expiration(refreshExpiredTime)
                .signWith(refreshTokenSigningKey)
                .compact();
        redisService.saveRefreshToken(loginId, refreshToken, refreshTokenExpirationSeconds);

        return refreshToken;
    }

    public String createTemporaryToken(String email, Date now) {
        long accessTokenExpirationMilliseconds = accessTokenExpirationSeconds * 1000;
        Date accessExpiredTime = new Date(now.getTime() + accessTokenExpirationMilliseconds);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(accessExpiredTime)
                .signWith(accessTokenSigningKey)
                .compact();
    }

    public String resolveAccessToken(HttpServletRequest request){
        String requestAccessTokenInHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith(BEARER_PREFIX)) {
            return requestAccessTokenInHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public Authentication getAuthenticationByAccessToken(String accessToken){
        Claims claims = getClaimsByAccessToken(accessToken);
        String subject = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateAccessToken(String token){
        return validateToken(accessTokenSigningKey, token);
    }

    public boolean validateRefreshToken(String token){
        return validateToken(refreshTokenSigningKey, token);
    }

    public Claims getClaimsByAccessToken(String accessToken){
        return getClaims(accessTokenSigningKey, accessToken);
    }

    public Claims getClaimsRefreshToken(String refreshToken){
        return getClaims(refreshTokenSigningKey, refreshToken);
    }

    public Claims getClaimsByTemporaryToken(String temporaryToken){
        return getClaims(accessTokenSigningKey, temporaryToken);
    }

    private Claims getClaims(SecretKey secretKey, String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            log.error("잘못된 jwt 토큰입니다. {}", e.getMessage());
            throw new IllegalArgumentException("잘못된 jwt 토큰 입니다.");
        }
    }

    private boolean validateToken(SecretKey key, String token) {
        if (token == null) {
            log.error("jwt 토큰이 비어 있습니다.");
            return false;
        }
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("잘못된 jwt 서명입니다.");
        } catch (MalformedJwtException e) {
            log.error("잘못된 jwt 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 jwt 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 jwt 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("jwt 클레임 문자열이 비어 있습니다.");
        }
        return false;
    }

    public void invalidateRefreshToken(String role, String subject) {
        redisService.deleteRefreshToken(subject);
    }

    public boolean validateRefreshTokenWithAccessTokenInfo(String role, String subject, String requestRefreshToken) {
        String refreshToken = redisService.getRefreshToken(subject);

        // 무결성 검증이 실패한 경우 초기화 진행
        if (!requestRefreshToken.equals(refreshToken)) {
            log.warn("{}-{}: reissue fail ({})", subject, role, new Date());
            redisService.deleteRefreshToken(subject);
            return false;
        }
        log.info("{}-{}: reissue ({})", subject, role, new Date());
        return true;
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    public long getRefreshTokenExpirationSeconds() {
        return refreshTokenExpirationSeconds;
    }
}