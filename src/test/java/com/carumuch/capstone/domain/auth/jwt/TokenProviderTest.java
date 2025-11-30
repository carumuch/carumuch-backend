package com.carumuch.capstone.domain.auth.jwt;

import com.carumuch.capstone.domain.auth.dto.CustomUserDetails;
import com.carumuch.capstone.domain.auth.service.CustomUserDetailsService;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.global.service.RedisService;
import com.carumuch.capstone.support.fixture.UserFixture;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static com.carumuch.capstone.global.constants.TokenConstant.AUTHORITIES_KEY;
import static com.carumuch.capstone.global.constants.TokenConstant.BEARER_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {
    private TokenProvider tokenProvider;

    private static final String TEST_ACCESS_SECRET = "accessabcdefghijklmnopqrstuvwxyz";
    private static final String TEST_REFRESH_SECRET = "refreshabcdefghijklmnopqrstuvwxyz";
    private static final long TEST_EXPIRATION = 3600;
    private static final String TEST_LOGIN_ID = "testLoginId";
    private static final String TEST_EMAIL = "test@gmail.com";
    private static final String TEST_ROLE_USER = "ROLE_USER";

    @Mock
    private RedisService redisService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(
                redisService,
                customUserDetailsService,
                TEST_ACCESS_SECRET,
                TEST_REFRESH_SECRET,
                TEST_EXPIRATION,
                TEST_EXPIRATION
        );
    }

    @Test
    void accessToken을_생성한다() {
        //when
        String accessToken = tokenProvider.createAccessToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());

        Claims claims = parseTokenSubject(accessToken, TEST_ACCESS_SECRET);
        String subject = claims.getSubject();
        String role = claims.get(AUTHORITIES_KEY).toString();

        //then
        assertAll(
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(subject).isEqualTo(TEST_LOGIN_ID),
                () -> assertThat(role).isEqualTo(TEST_ROLE_USER)
        );
    }

    @Test
    void refreshToken을_생성한다() {
        //when
        String refreshToken = tokenProvider.createRefreshToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());
        String subject = parseTokenSubject(refreshToken, TEST_REFRESH_SECRET).getSubject();

        //then
        assertAll(
                () -> assertThat(refreshToken).isNotNull(),
                () -> assertThat(subject).isEqualTo(TEST_LOGIN_ID)
        );
        verify(redisService, times(1)).saveRefreshToken(TEST_LOGIN_ID, refreshToken, TEST_EXPIRATION);
    }

    @Test
    void temporaryToken을_생성한다() {
        //when
        String temporaryToken = tokenProvider.createTemporaryToken(TEST_EMAIL, new Date());

        Claims claims = parseTokenSubject(temporaryToken, TEST_ACCESS_SECRET);
        String subject = claims.getSubject();

        //then
        assertAll(
                () -> assertThat(temporaryToken).isNotNull(),
                () -> assertThat(subject).isEqualTo(TEST_EMAIL)
        );
    }

    @Test
    void 사용자_권한_정보를_accessToken으로_조회한다() {
        //given
        User user = UserFixture.USER_FIXTURE_1.createUser();

        String accessToken = tokenProvider.createAccessToken(user.getLoginId(), user.getRole().getKey(), new Date());
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        when(customUserDetailsService.loadUserByUsername(user.getLoginId())).thenReturn(customUserDetails);

        //when
        Authentication authentication = tokenProvider.getAuthenticationByAccessToken(accessToken);

        //then
        assertAll(
                () -> assertThat(authentication).isNotNull(),
                () -> assertThat(authentication.getName()).isEqualTo(user.getLoginId()),
                () -> assertThat(authentication.getAuthorities())
                        .hasSize(1)
                        .extracting(authority -> authority.getAuthority())
                        .containsExactly(user.getRole().getKey())
        );
    }

    @Test
    void accessToken으로_Claims를_조회한다() {
        //given
        String accessToken = tokenProvider.createAccessToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());

        //when
        Claims claims = tokenProvider.getClaimsByAccessToken(accessToken);
        String subject = claims.getSubject();
        String role = claims.get(AUTHORITIES_KEY).toString();


        //then
        assertAll(
                () -> assertThat(subject).isEqualTo(TEST_LOGIN_ID),
                () -> assertThat(role).isEqualTo(TEST_ROLE_USER)
        );
    }

    @Test
    void refreshToken으로_Claims를_조회한다() {
        //given
        String refreshToken = tokenProvider.createRefreshToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());

        //when
        Claims claims = tokenProvider.getClaimsRefreshToken(refreshToken);
        String subject = claims.getSubject();

        //then
        assertThat(subject).isEqualTo(TEST_LOGIN_ID);
    }

    @Test
    void temporaryToken으로_Claims를_조회한다() {
        //given
        String temporaryToken = tokenProvider.createTemporaryToken(TEST_EMAIL, new Date());

        //when
        Claims claims = tokenProvider.getClaimsByTemporaryToken(temporaryToken);
        String subject = claims.getSubject();

        //then
        assertThat(subject).isEqualTo(TEST_EMAIL);
    }

    @Test
    void 요청에서_accessToken을_추출한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION))
                .thenReturn(BEARER_PREFIX + "testAccessToken");

        //when
        String accessToken = tokenProvider.resolveAccessToken(request);

        //then
        assertThat(accessToken).isEqualTo("testAccessToken");
    }

    @Test
    void 요청의_accessToken에_Bearer이_포함되지않으면_null을_반환한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn("noBearer testAccessToken");

        // when
        String result = tokenProvider.resolveAccessToken(request);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 요청의_accessToken이_존재하지않으면_null을_반환한다() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        // when
        String result = tokenProvider.resolveAccessToken(request);

        // then
        assertThat(result).isNull();
    }

    @Test
    void accessToken의_유효성을_검증한다() {
        //given
        String accessToken = tokenProvider.createAccessToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());

        //when
        boolean isValid = tokenProvider.validateAccessToken(accessToken);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    void 만료된_accessToekn을_검증한다() {
        // given
        String expiredAccessToken = tokenProvider.createAccessToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date(System.currentTimeMillis() - TEST_EXPIRATION * 1000));
        // when
        boolean isValid = tokenProvider.validateAccessToken(expiredAccessToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void refreshToken의_유효성을_검증한다() {
        //given
        String refreshToken = tokenProvider.createRefreshToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());

        //when
        boolean isValid = tokenProvider.validateRefreshToken(refreshToken);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    void 만료된_refreshToken을_검증한다() {
        // given
        String expiredRefreshToken = tokenProvider.createRefreshToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date(System.currentTimeMillis() - TEST_EXPIRATION * 1000));

        // when
        boolean isValid = tokenProvider.validateAccessToken(expiredRefreshToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 재발급_토큰을_무효화한다() {
        //when
        tokenProvider.invalidateRefreshToken(TEST_ROLE_USER, TEST_LOGIN_ID);

        //then
        verify(redisService, times(1))
                .deleteRefreshToken(TEST_LOGIN_ID);
    }

    @Test
    void 요청된_refreshToken이_Redis의_토큰과_일치한다() {
        //given
        String requestRefreshToken = "validRefreshToken";
        when(redisService.getRefreshToken(TEST_LOGIN_ID)).thenReturn(requestRefreshToken);

        //when
        boolean isValid = tokenProvider.validateRefreshTokenWithAccessTokenInfo(TEST_ROLE_USER, TEST_LOGIN_ID, requestRefreshToken);

        //then
        assertThat(isValid).isTrue();
        verify(redisService, times(1)).getRefreshToken(TEST_LOGIN_ID);
    }

    @Test
    void 요청된_refreshToken이_Redis의_토큰과_불일치한다() {
        //given
        String requestRefreshToken = "invalidRefreshToken";
        String redisRefreshToken = "refreshToken";
        when(redisService.getRefreshToken(TEST_LOGIN_ID)).thenReturn(redisRefreshToken);

        //when
        boolean isValid = tokenProvider.validateRefreshTokenWithAccessTokenInfo(TEST_ROLE_USER, TEST_LOGIN_ID, requestRefreshToken
        );

        //then
        assertThat(isValid).isFalse();
        verify(redisService, times(1)).getRefreshToken(TEST_LOGIN_ID);
        verify(redisService, times(1)).deleteRefreshToken(TEST_LOGIN_ID);
    }

    @Test
    void 요청된_accessToken정보에_맞는_refreshToken이_Redis에_없다() {
        //given
        String requestRefreshToken = "anyRefreshToken";
        when(redisService.getRefreshToken(TEST_LOGIN_ID)).thenReturn(null);

        //when
        boolean isValid = tokenProvider.validateRefreshTokenWithAccessTokenInfo(TEST_ROLE_USER, TEST_LOGIN_ID, requestRefreshToken
        );

        //then
        assertThat(isValid).isFalse();
        verify(redisService, times(1)).getRefreshToken(TEST_LOGIN_ID);
        verify(redisService, times(1)).deleteRefreshToken(TEST_LOGIN_ID);
    }

    @Test
    void 지정된_accessToken_만료시간을_조회한다() {
        //when
        long accessTokenExpirationSeconds = tokenProvider.getAccessTokenExpirationSeconds();

        //then
        assertThat(accessTokenExpirationSeconds).isEqualTo(TEST_EXPIRATION);

    }

    @Test
    void 지정된_refreshToken_만료시간을_조회한다() {
        //when
        long refreshTokenExpirationSeconds = tokenProvider.getRefreshTokenExpirationSeconds();

        //then
        assertThat(refreshTokenExpirationSeconds).isEqualTo(TEST_EXPIRATION);
    }

    @Test
    void 만료된_토큰을_파싱하려고_하면_만료된_Claims를_반환한다() {
        // given
        String expiredToken = tokenProvider.createAccessToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date(System.currentTimeMillis() - TEST_EXPIRATION * 1000));

        // when
        Claims claims = tokenProvider.getClaimsByAccessToken(expiredToken);

        // then
        assertThat(claims).isNotNull();
    }

    @Test
    void 잘못된_서명의_토큰을_검증하면_false를_반환한다() {
        // given
        String accessToken = tokenProvider.createAccessToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());
        String invalidSignature = "invalidSignature";
        accessToken += invalidSignature;

        // when
        boolean isValid = tokenProvider.validateAccessToken(accessToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 잘못된_형식의_토큰을_검증하면_false를_반환한다() {
        // given
        String malformedToken = "malformedToken";

        // when
        boolean isValid = tokenProvider.validateAccessToken(malformedToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 지원되지_않는_토큰을_검증하면_false를_반환한다() {
        // given
        String unsupportedToken = tokenProvider.createAccessToken(TEST_LOGIN_ID, TEST_ROLE_USER, new Date());

        // when
        boolean isValid = tokenProvider.validateAccessToken(unsupportedToken.replace(".", "/"));

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 토큰이_null이면_false를_반환한다() {
        // given
        String nullToken = null;

        // when
        boolean isValid = tokenProvider.validateAccessToken(nullToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 비어있는_토큰을_검증하면_false를_반환한다() {
        // given
        String emptyToken = "";

        // when
        boolean isValid = tokenProvider.validateAccessToken(emptyToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 비어있는_Claims_토큰을_검증하면_false를_반환한다() {
        // given
        String emptyClaimToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(TEST_ACCESS_SECRET.getBytes()))
                .compact();

        // when
        boolean isValid = tokenProvider.validateAccessToken(emptyClaimToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 잘못된_토큰이면_에러가_발생한다() {
        // given
        String invalidToken = "invalidToken";

        // when & then
        assertThatThrownBy(() -> tokenProvider.getClaimsByAccessToken(invalidToken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Claims parseTokenSubject(String token, String secretKey) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}