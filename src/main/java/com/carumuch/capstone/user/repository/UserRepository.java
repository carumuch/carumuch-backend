package com.carumuch.capstone.user.repository;

import com.carumuch.capstone.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /* 로그인 아이디 중복 체크 */
    boolean existsByLoginId(String loginId);

    /* 이메일 중복 체크 */
    boolean existsByEmail(String email);

    /**
     * select: 아이디로 회원 검색
     * 1. security
     */
    Optional<User> findByLoginId(String loginId);

    /**
     * select: 로그인 된 회원 검색
     */
    User findLoginUserByLoginId(String loginId);

    /**
     * DELETE
     * 1. 회원 탈퇴
     */
    void deleteByLoginId(String loginId);

    /**
     * Select
     * 1. OAuth2 유저 갱신 사용
     */
    User findOAuth2UserByLoginId(String loginId);

}
