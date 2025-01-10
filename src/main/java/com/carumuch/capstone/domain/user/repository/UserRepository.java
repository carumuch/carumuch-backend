package com.carumuch.capstone.domain.user.repository;

import com.carumuch.capstone.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
     * select: 이메일로 회원 검색
     * 1. 비밀번호 찾기
     */
    Optional<User> findByEmail(String email);

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

    /**
     * select: 아이디로 유저와 차량 조회 (Lazy 초기화)
     * 1. 유저가 가지고 있는 차량 목록 조회
     * info: 요구사항 변경 -> 차량 2대 이상에서 1대만 가지는것으로 변경 되었습니다.
     * Date: 2024.10.07
     */
//    @Query("select u from User u left join fetch u.vehicles where u.loginId =:loginId")
//    User findByLoginIdWithVehicle(@Param("loginId") String loginId);

    /**
     * select: 아이디로 유저와 공업사 조회 (Lazy 초기화)
     * 1. 공업사 정보 수정
     */
    @Query("select u from User u left join fetch u.bodyShop where u.loginId = :loginId")
    User findByLoginIdWithBodyShop(@Param("loginId") String loginId);

}
