package com.techwave.olol.login.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.techwave.olol.global.exception.ApiException;
import com.techwave.olol.global.exception.Error;
import com.techwave.olol.login.auth.JwtProvider;
import com.techwave.olol.login.config.KakaoProperties;
import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.login.dto.AuthTokenDto;
import com.techwave.olol.login.dto.TokenDto;
import com.techwave.olol.login.dto.reponse.KakaoAuthResponse;
import com.techwave.olol.login.dto.reponse.KakaoUserInfoResponse;
import com.techwave.olol.login.model.RefreshToken;
import com.techwave.olol.login.repository.RefreshTokenRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;
	private final KakaoProperties kakaoProperties;

	private final WebClient kakaoAuthClient = WebClient.builder().baseUrl("https://kauth.kakao.com").build();
	private final WebClient kakaoUserClient = WebClient.builder().baseUrl("https://kapi.kakao.com").build();

	public AuthTokenDto kakaoLogin(String code) {

		log.info("Starting Kakao login with code: {}", code);
		//authorizationCode로 kakao accessToken 요청
		MultiValueMap<String, String> authRequest = authRequest(code);
		KakaoAuthResponse authResponse = getAccessToken(authRequest);

		//accessToken로 유저정보 요청
		KakaoUserInfoResponse userInfo = getUserInfo(authResponse.getAccessToken());

		boolean isJoined = false;
		Optional<User> userOpt = userRepository.findBySnsId(userInfo.getId().toString());
		User user;
		if (userOpt.isEmpty()) {
			User newUser = User.builder().authType(AuthType.KAKAO).snsId(userInfo.getId().toString()).build();
			user = userRepository.save(newUser);
		} else {
			user = userOpt.get();
			if (user.getAuthType() != AuthType.KAKAO)
				throw new ApiException(Error.AUTH_TYPE_MISMATCH);
			if (!StringUtils.isEmpty(user.getNickname()))
				isJoined = true; // 닉네임 존재 여부로 회원가입 추가 정보 입력 화면으로 이동
		}

		//토큰 발급
		TokenDto accessTokenDto = jwtProvider.generateToken(user.getId());
		TokenDto refreshTokenDto = jwtProvider.generateRefreshToken();

		Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByUserId(user.getId());
		RefreshToken refreshToken;
		if (refreshTokenOpt.isEmpty()) {
			refreshToken = RefreshToken.builder()
				.userId(user.getId())
				.build();
		} else {
			refreshToken = refreshTokenOpt.get();
		}

		refreshToken.setRefreshToken(refreshTokenDto.getToken());
		refreshToken.setExpiration(refreshTokenDto.getExpiration());

		refreshTokenRepository.save(refreshToken);

		return new AuthTokenDto(isJoined, accessTokenDto, refreshTokenDto);
	}

	private KakaoAuthResponse getAccessToken(MultiValueMap<String, String> request) {
		return kakaoAuthClient.post()
			.uri("/oauth/token")
			.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
			.body(BodyInserters.fromFormData(request))
			.retrieve()
			.bodyToMono(KakaoAuthResponse.class)
			.block();
	}

	private KakaoUserInfoResponse getUserInfo(String accessToken) {
		return kakaoUserClient.get()
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
		authRequest.add("client_id", kakaoProperties.getClientId());
		authRequest.add("client_secret", kakaoProperties.getClientSecret());
		authRequest.add("redirect_uri", kakaoProperties.getRedirectUri());
		authRequest.add("code", code);

		return authRequest;
	}
}
