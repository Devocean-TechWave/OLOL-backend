package com.techwave.olol.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

	Optional<RefreshToken> findByUserId(String userId);
}
