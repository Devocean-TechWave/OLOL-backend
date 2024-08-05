package com.techwave.olol.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.user.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByNickname(String nickname);

	Optional<User> findBySnsId(String id);
}

