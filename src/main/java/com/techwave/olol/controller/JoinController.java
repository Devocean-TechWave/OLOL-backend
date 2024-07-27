package com.techwave.olol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.dto.reponse.ApiResponse;
import com.techwave.olol.dto.request.KakaoJoinRequest;
import com.techwave.olol.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/v1/users/join")
@RestController
public class JoinController {

	private final UserService userService;

	@Operation(summary = "카카오 (첫 로그인 = 회원가입 시) 추가 정보")
	@PostMapping("/kakao")
	public ResponseEntity<ApiResponse> joinKakao(Authentication authentication,
		@RequestBody @Valid KakaoJoinRequest request) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		userService.kakaoJoin(userDetails.getUsername(), request);

		return ResponseEntity.ok().body(new ApiResponse(true));
	}
}
