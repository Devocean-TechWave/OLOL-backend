package com.techwave.olol.login.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KakaoAuthResponse {

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private String expiresIn;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("refresh_token_expires_in")
	private String refreshTokenExpiresIn;
}
