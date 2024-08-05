package com.techwave.olol.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.login.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

	Optional<RefreshToken> findByUserId(String userId);
}
