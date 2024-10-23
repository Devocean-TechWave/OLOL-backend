package com.techwave.olol.auth.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

	@Id
	private String userId;

	@Setter
	private String refreshToken;

	@Setter
	private LocalDateTime expiration;

	@Builder
	public RefreshToken(String userId, String refreshToken, LocalDateTime expiration) {
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.expiration = expiration;
	}
}

