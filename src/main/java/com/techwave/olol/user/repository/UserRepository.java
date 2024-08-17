package com.techwave.olol.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.user.domain.User;

import jakarta.persistence.EntityNotFoundException;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByNickname(String nickname);

	Optional<User> findBySnsId(String id);

	default User findUserById(String userId) {
		return findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다: " + userId));
	}
}

