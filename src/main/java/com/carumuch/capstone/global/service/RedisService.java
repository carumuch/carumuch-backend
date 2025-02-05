package com.carumuch.capstone.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public final String REFRESH_PREFIX = "RT:";
    private final String OAUTH2_PREFIX = "AT:";
    private final String CODE_PREFIX = "CODE:";

    public void save(String key, String value, long expirationSeconds) {
        redisTemplate.opsForValue().set(key, value, expirationSeconds, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public void saveRefreshToken(String loginId, String refreshToken, long refreshTokenExpirationSeconds) {
        redisTemplate.opsForValue().set(REFRESH_PREFIX + loginId, refreshToken, refreshTokenExpirationSeconds, TimeUnit.SECONDS);
    }

    public String getRefreshToken(String loginId) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + loginId);
    }

    public Boolean deleteRefreshToken(String loginId) {
        return redisTemplate.delete(REFRESH_PREFIX + loginId);
    }

    public void saveOauth2AccessToken(String loginId, String oauth2AccessToken, long oauth2ValidityInMilliseconds) {
        redisTemplate.opsForValue().set(OAUTH2_PREFIX + loginId, oauth2AccessToken, oauth2ValidityInMilliseconds, TimeUnit.SECONDS);
    }

    public String getOauth2AccessToken(String loginId) {
        return redisTemplate.opsForValue().get(OAUTH2_PREFIX + loginId);
    }

    public Boolean deleteOauth2AccessToken(String loginId) {
        return redisTemplate.delete(OAUTH2_PREFIX + loginId);
    }

    public void saveCode(String email, String code, long codeValidityInMilliseconds) {
        redisTemplate.opsForValue().set(CODE_PREFIX + email, code, codeValidityInMilliseconds, TimeUnit.SECONDS);
    }

    public String getCode(String email) {
        return redisTemplate.opsForValue().get(CODE_PREFIX + email);
    }

    public Boolean deleteCode(String email) {
        return redisTemplate.delete(CODE_PREFIX + email);
    }
}
