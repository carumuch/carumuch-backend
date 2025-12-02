package com.carumuch.capstone.identity.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carumuch.capstone.identity.domain.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	Optional<User> findByLoginId(String loginId);
	Optional<User> findByEmail(String email);
	boolean existsByLoginId(String loginId);
	boolean existsByEmail(String email);
	Optional<User> findByLoginIdAndEmail(String loginId, String email);
}
