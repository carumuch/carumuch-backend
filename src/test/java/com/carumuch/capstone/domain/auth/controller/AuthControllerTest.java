package com.carumuch.capstone.domain.auth.controller;

import com.carumuch.capstone.support.annotation.WithMockCustom;
import com.module.RestDocsSupport;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Date;

import static com.carumuch.capstone.global.constants.TokenConstant.*;
import static com.carumuch.capstone.global.constants.TokenConstant.AUTHORITIES_KEY;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AuthControllerTest extends RestDocsSupport {

    private final String ROLE_USER = "ROLE_USER";
    private final String TEST_SUBJECT = "testLoginId";
    private final String TEST_ACCESS_TOKEN = "testAccessToken";

    @Test
    @WithMockCustom(role = ROLE_USER)
    void API_로그아웃_테스트() throws Exception {
        //given
        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(tokenProvider.resolveAccessToken(Mockito.any(HttpServletRequest.class)))
                .thenReturn(TEST_ACCESS_TOKEN);
        Mockito.when(tokenProvider.getClaimsByAccessToken(TEST_ACCESS_TOKEN))
                .thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(TEST_SUBJECT);
        Mockito.when(claims.get(AUTHORITIES_KEY)).thenReturn(ROLE_USER);

        //when
        ResultActions actions = mockMvc.perform(
                post("/logout")
                        .header(AUTHORIZATION, BEARER_PREFIX + TEST_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.response").isEmpty())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refresh_token=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")))
                .andDo(restDocsHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {accessToken})")),
                        responseCookies(
                                cookieWithName(REFRESH_TOKEN_COOKIE_NAME).description("쿠키의 refreshToken 초기화")),
                        responseFields(
                                fieldWithPath("success").description("true"),
                                fieldWithPath("status").description("200"),
                                fieldWithPath("code").description("OK"),
                                fieldWithPath("message").description("성공적으로 처리되었습니다."),
                                fieldWithPath("response").description("null"))
                        )
                )
                .andDo(print());
    }

    @Test
    @WithMockCustom(role = ROLE_USER)
    void API_토큰_재발급_요청_테스트() throws Exception {
        // given
        String requestRefreshToken = "testRefreshToken";

        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(tokenProvider.resolveAccessToken(Mockito.any(HttpServletRequest.class))).thenReturn(TEST_ACCESS_TOKEN);
        Mockito.when(tokenProvider.getClaimsByAccessToken(TEST_ACCESS_TOKEN)).thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn(TEST_SUBJECT);
        Mockito.when(claims.get(AUTHORITIES_KEY)).thenReturn(ROLE_USER);
        when(tokenProvider.validateRefreshTokenWithAccessTokenInfo(ROLE_USER, TEST_SUBJECT, requestRefreshToken)).thenReturn(true);

        String newAccessToken = "newAccessToken";
        Mockito.when(tokenProvider.createAccessToken(eq(TEST_SUBJECT), eq(ROLE_USER), any(Date.class))).thenReturn(newAccessToken);

        // when
        ResultActions actions = mockMvc.perform(
                post("/reissue")
                        .header(AUTHORIZATION, BEARER_PREFIX + TEST_ACCESS_TOKEN)
                        .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, requestRefreshToken))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.response").isEmpty())
                .andExpect(header().string(AUTHORIZATION, newAccessToken))
                .andDo(restDocsHandler.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {accessToken})")),
                                responseHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer {accessToken})")),
                                responseFields(
                                        fieldWithPath("success").description("true"),
                                        fieldWithPath("status").description("200"),
                                        fieldWithPath("code").description("OK"),
                                        fieldWithPath("message").description("성공적으로 처리되었습니다."),
                                        fieldWithPath("response").description("null"))
                        )
                )
                .andDo(print());
    }
}