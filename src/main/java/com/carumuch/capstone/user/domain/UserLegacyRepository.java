package com.carumuch.capstone.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// TODO 의존성이 모두 덜어지면 제거합니다.
public interface UserLegacyRepository extends JpaRepository<User, Long> {

    User findLoginUserByLoginId(String loginId);

    @Query("select u from User u left join fetch u.bodyShop where u.loginId = :loginId")
    User findByLoginIdWithBodyShop(@Param("loginId") String loginId);

}
