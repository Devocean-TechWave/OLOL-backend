package com.techwave.olol.login.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.login.dto.AuthTokenDto;
import com.techwave.olol.login.dto.reponse.ResponseDto;
import com.techwave.olol.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/v1/users/login")
@RestController

public class LoginController {

	private final LoginService loginService;

	@Operation(
		summary = "카카오 로그인",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "카카오 로그인 성공",
				content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = AuthTokenDto.class)
				)
			)
		}
	)
	@GetMapping("/kakao")
	public ResponseEntity<ResponseDto> kakaoLogin(@RequestParam(value = "code") String code) {
		AuthTokenDto dto = loginService.kakaoLogin(code);

		return ResponseEntity.ok().body(new ResponseDto(dto));
	}
}

