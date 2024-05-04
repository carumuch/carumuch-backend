package com.carumuch.capstone.user.repository;

import com.carumuch.capstone.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 로그인 아이디 중복 체크
     */
    boolean existsByLoginId(String loginId);

    /**
     * 이메일 중복 체크
     */
    boolean existsByEmail(String email);

}
