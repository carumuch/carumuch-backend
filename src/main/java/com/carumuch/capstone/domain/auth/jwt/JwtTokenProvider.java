package com.carumuch.capstone.domain.auth.jwt;


import com.carumuch.capstone.domain.auth.dto.TokenDto;
import com.carumuch.capstone.domain.auth.dto.CustomUserDetails;
import com.carumuch.capstone.global.service.RedisService;
import com.carumuch.capstone.domain.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;


@Slf4j
@Component
@Transactional(readOnly = true)
public class JwtTokenProvider implements InitializingBean {

    private final CustomUserDetailsService userDetailsService;
    private final RedisService redisService;

    private static final String AUTHORITIES_KEY = "role";
    private static final String LOGIN_ID_KEY = "loginId";
    private static final String EMAIL_KEY = "email";
    private final String url;

    private final String secretKey;
    private static Key signingKey;

    private final Long accessTokenValidityInMilliseconds;
    private final Long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
            CustomUserDetailsService userDetailsService,
            RedisService redisService,
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.access-token-validity-in-seconds}") Long accessTokenValidityInMilliseconds,
            @Value("${spring.jwt.refresh-token-validity-in-seconds}") Long refreshTokenValidityInMilliseconds,
            @Value("${server.host}") String url) {
        this.userDetailsService = userDetailsService;
        this.redisService = redisService;
        this.secretKey = secretKey;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
        this.url = url;
    }

    /* 시크릿 키 설정 */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    /* Create: 토큰 생성 */
    @Transactional
    public TokenDto createToken(String loginId, String authorities){
        Long now = System.currentTimeMillis();

        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + accessTokenValidityInMilliseconds))
                .setSubject("access-token")
                .claim(url, true)
                .claim(LOGIN_ID_KEY, loginId)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + refreshTokenValidityInMilliseconds))
                .setSubject("refresh-token")
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    /* Create: 임시 토큰 생성 */
    @Transactional
    public String createTemporaryToken(String email){
        Long now = System.currentTimeMillis();

        long temporaryTokenValidityInMilliseconds = 600 * 1000; // 10분

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + temporaryTokenValidityInMilliseconds))
                .setSubject("temporary-token")
                .claim(url, true)
                .claim(EMAIL_KEY, email)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }



    /* 토큰 추출 */
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) { // Access Token
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String token) {
        String loginId = getClaims(token).get(LOGIN_ID_KEY).toString();
        CustomUserDetails customUserDetails = userDetailsService.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
    }

    public long getTokenExpirationTime(String token) {
        return getClaims(token).getExpiration().getTime();
    }


    /* Refresh token 검증 */
    public boolean validateRefreshToken(String refreshToken){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException e) {
            log.error("error-> Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("error-> Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("error-> Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("error-> Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("error-> JWT claims string is empty.");
        } catch (NullPointerException e){
            log.error("error-> JWT Token is empty.");
        }
        return false;
    }

    /* Access Token 검증 */
    public boolean validateAccessToken(String accessToken) {
        try {
            /* 로그아웃 토큰 무효화 */
            if (redisService.getValues(accessToken) != null
                    && redisService.getValues(accessToken).equals("logout")) {
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch(ExpiredJwtException e) {
            // 검증 요청을 거치지 않고 악의적으로 토큰을 던졌을 때
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /* 토큰 만료 시간 검증 */
    public boolean validateAccessTokenOnlyExpired(String accessToken) {
        try {
            return getClaims(accessToken)
                    .getExpiration()
                    .before(new Date());
        } catch(ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}