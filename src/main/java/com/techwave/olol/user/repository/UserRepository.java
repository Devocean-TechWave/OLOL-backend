package com.techwave.olol.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.user.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByNickname(String nickname);

	Optional<User> findById(String id);

	Optional<User> findBySnsId(String id);
}

