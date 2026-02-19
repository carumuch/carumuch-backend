package com.carumuch.capstone.identity.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// TODO 의존성이 모두 덜어지면 제거합니다.
public interface UserLegacyRepository extends JpaRepository<User, Long> {

    User findLoginUserByLoginId(String loginId);
}
