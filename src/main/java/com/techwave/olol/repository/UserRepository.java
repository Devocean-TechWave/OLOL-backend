package com.techwave.olol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByNickname(String nickname);

	User findBySnsId(String id);
}
