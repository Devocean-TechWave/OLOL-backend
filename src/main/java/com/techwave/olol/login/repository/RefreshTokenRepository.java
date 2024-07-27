package com.techwave.olol.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.login.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

	RefreshToken findByUserId(String userId);

	RefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
