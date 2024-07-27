package com.techwave.olol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

	RefreshToken findByUserId(String userId);

	RefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
