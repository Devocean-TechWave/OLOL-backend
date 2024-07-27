package com.techwave.olol.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KakaoUserInfoResponse {

	private Long id;

	@JsonProperty("kakao_account")
	private Object kakaoAccout;

	private Object properties;
}
