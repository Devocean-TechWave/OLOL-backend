package com.techwave.olol.login.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

	@Id
	private String userId;

	private String refreshToken;

	private LocalDateTime expiration;

	@Builder
	public RefreshToken(String userId, String refreshToken, LocalDateTime expiration) {
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.expiration = expiration;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}
}
