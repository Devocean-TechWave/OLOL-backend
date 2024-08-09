package com.techwave.olol.user.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.login.dto.AuthTokenDto;
import com.techwave.olol.login.service.LoginService;
import com.techwave.olol.user.dto.UserDto;
import com.techwave.olol.user.dto.request.KakaoJoinRequest;
import com.techwave.olol.user.service.ImagesService;
import com.techwave.olol.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
@Tag(name = "User", description = "회원정보 조회 및 수정 API")
public class UserController {

	private final UserService userService;
	private final LoginService loginService;
	private final ImagesService imagesService;

	// 유저 검색 및 닉네임 중복 체크
	@Operation(summary = "유저 검색(nickname)",
		responses =
			{
				@ApiResponse(
					responseCode = "200",
					description = "유저 검색 성공",
					content = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema = @Schema(implementation = UserDto.class)
					)
				)
			}
	)
	@GetMapping
	public ResponseEntity<UserDto> findByNickname(@RequestParam(value = "nickname") String nickname) {
		UserDto dto = userService.findByNickname(nickname);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "닉네임 중복 체크",
		responses =
			{
				@ApiResponse(
					responseCode = "200",
					description = "닉네임 중복 체크 성공",
					content = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema = @Schema(implementation = Boolean.class)
					)
				)
			}
	)
	@GetMapping("/check-nickname")
	public ResponseEntity<Boolean> checkNickname(@RequestParam(value = "nickname") String nickname) {
		boolean existsNickname = userService.checkNickname(nickname);
		return ResponseEntity.ok(existsNickname);
	}

	@Operation(summary = "유저 정보 조회",
		responses =
			{
				@ApiResponse(
					responseCode = "200",
					description = "조회 성공",
					content = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema = @Schema(implementation = UserDto.class)
					)
				)
			}
	)
	@GetMapping("/info/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable String id) {
		UserDto dto = userService.getUser(id);
		return ResponseEntity.ok(dto);
	}

	// 카카오 로그인
	@Operation(
		summary = "카카오 로그인",
		responses =
			{
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
	@GetMapping("/login/kakao")
	public ResponseEntity<AuthTokenDto> kakaoLogin(@RequestParam(value = "code") String code) {
		System.out.println("code = " + code);
		AuthTokenDto dto = loginService.kakaoLogin(code);
		return ResponseEntity.ok(dto);
	}

	// 카카오 회원가입
	@Operation(summary = "카카오 (첫 로그인 = 회원가입 시) 추가 정보",
		responses =
			{
				@ApiResponse(
					responseCode = "200",
					description = "회원가입 성공",
					content = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema = @Schema(implementation = Boolean.class)
					)
				)
			}
	)
	@PostMapping("/join/kakao")
	public ResponseEntity<Boolean> joinKakao(
		Authentication authentication,
		@RequestBody @Valid KakaoJoinRequest request) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		userService.kakaoJoin(userDetails.getUsername(), request);
		return ResponseEntity.ok(true);
	}

	// 이미지 관련
	@Operation(summary = "이미지 URL")
	@GetMapping("/images/profile/{id}/{filename}")
	public ResponseEntity<Resource> getProfileImages(
		@PathVariable String id,
		@PathVariable String filename) {
		return imagesService.getProfileImages(id, filename);
	}
}




