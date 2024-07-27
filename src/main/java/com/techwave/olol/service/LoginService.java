package com.techwave.olol.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.techwave.olol.auth.JwtProvider;
import com.techwave.olol.constant.AuthType;
import com.techwave.olol.constant.Error;
import com.techwave.olol.dto.AuthTokenDto;
import com.techwave.olol.dto.TokenDto;
import com.techwave.olol.dto.reponse.KakaoAuthResponse;
import com.techwave.olol.dto.reponse.KakaoUserInfoResponse;
import com.techwave.olol.exception.ApiException;
import com.techwave.olol.model.RefreshToken;
import com.techwave.olol.model.User;
import com.techwave.olol.repository.RefreshTokenRepository;
import com.techwave.olol.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	private final JwtProvider jwtProvider;

	private final WebClient webClient1 = WebClient.builder().baseUrl("https://kauth.kakao.com").build();
	private final WebClient webClient2 = WebClient.builder().baseUrl("https://kapi.kakao.com").build();

	@Value("${kakao.client-id}")
	private String clientId;

	@Value("${kakao.client-secret}")
	private String clientSecret;

	@Value("${kakao.redirect-uri}")
	private String redirectUri;

	public AuthTokenDto kakaoLogin(String code) {
		//authorizationCode로 kakao accessToken 요청
		MultiValueMap<String, String> authRequest = authRequest(code);
		KakaoAuthResponse authResponse = getAccessToken(authRequest);

		//accessToken로 유저정보 요청
		KakaoUserInfoResponse userInfo = getUserInfo(authResponse.getAccessToken());

		boolean isJoined = false;
		User user = userRepository.findBySnsId(userInfo.getId().toString());
		if (user == null) {
			User newUser = User.builder().authType(AuthType.KAKAO).snsId(userInfo.getId().toString()).build();
			user = userRepository.save(newUser);
		} else {
			if (user.getAuthType() != AuthType.KAKAO)
				throw new ApiException(Error.AUTH_TYPE_MISMATCH);
			if (!StringUtils.isEmpty(user.getNickname()))
				isJoined = true; // 닉네임 존재 여부로 회원가입 추가 정보 입력 화면으로 이동
		}

		//토큰 발급
		TokenDto accessTokenDto = jwtProvider.generateToken(user.getId());
		TokenDto refreshTokenDto = jwtProvider.generateRefreshToken();

		RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
		if (refreshToken == null) {
			refreshToken = RefreshToken.builder()
				.userId(user.getId())
				.build();
		}

		refreshToken.setRefreshToken(refreshTokenDto.getToken());
		refreshToken.setExpiration(refreshTokenDto.getExpiration());

		refreshTokenRepository.save(refreshToken);

		return new AuthTokenDto(isJoined, accessTokenDto, refreshTokenDto);
	}

	private KakaoAuthResponse getAccessToken(MultiValueMap<String, String> request) {
		return webClient1.post()
			.uri("/oauth/token")
			.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
			.body(BodyInserters.fromFormData(request))
			.retrieve()
			.bodyToMono(KakaoAuthResponse.class)
			.block();
	}

	private KakaoUserInfoResponse getUserInfo(String accessToken) {
		return webClient2.get()
			.uri("/v2/user/me")
			.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
			.header("Authorization", "Bearer " + accessToken)
			.retrieve()
			.bodyToMono(KakaoUserInfoResponse.class)
			.block();
	}

	private MultiValueMap<String, String> authRequest(String code) {
		MultiValueMap<String, String> authRequest = new LinkedMultiValueMap<>();
		authRequest.add("grant_type", "authorization_code");
		authRequest.add("client_id", clientId);
		authRequest.add("client_secret", clientSecret);
		authRequest.add("redirect_uri", redirectUri);
		authRequest.add("code", code);

		return authRequest;
	}
}
