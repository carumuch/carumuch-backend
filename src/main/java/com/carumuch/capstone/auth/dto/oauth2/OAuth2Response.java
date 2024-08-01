package com.carumuch.capstone.auth.dto.oauth2;

public interface OAuth2Response {

    /*제공자 (kako, naver, google) */
    String getProvider();
    /* 제공자 발급 아이디 */
    String getProviderId();
    /* 사용자 이메일 */
    String getEmail();
    /* 사용자 이름 */
    String getName();
}
