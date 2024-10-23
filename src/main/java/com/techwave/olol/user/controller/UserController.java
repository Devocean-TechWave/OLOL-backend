package com.techwave.olol.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techwave.olol.login.dto.AuthTokenDto;
import com.techwave.olol.login.service.LoginService;
import com.techwave.olol.user.dto.NickNameResDto;
import com.techwave.olol.user.dto.UserDto;
import com.techwave.olol.user.dto.UserInfoDto;
import com.techwave.olol.user.dto.request.KakaoJoinRequestDto;
import com.techwave.olol.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
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

	// 카카오 로그인
	@Operation(summary = "카카오 로그인")
	@GetMapping("/login/kakao")
	public ResponseEntity<AuthTokenDto> kakaoLogin(@RequestParam(value = "code") String code) {
		System.out.println("code = " + code);
		AuthTokenDto dto = loginService.kakaoLogin(code);
		return ResponseEntity.ok(dto);
	}

	// 카카오 회원가입
	@Operation(summary = "카카오 (첫 로그인 = 회원가입 시) 추가 정보")
	@PostMapping("/join")//TODO: 이미 가입 되어있는 경우 오류 처리
	public ResponseEntity<UserDto> joinKakao(
		Authentication authentication,
		@RequestBody @Valid KakaoJoinRequestDto request) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		return ResponseEntity.ok(new UserDto(userService.kakaoJoin(userDetails.getUsername(), request)));
	}

	@Operation(summary = "닉네임 중복 체크")
	@GetMapping("/check-nickname")
	public ResponseEntity<NickNameResDto> checkNickname(@RequestParam(value = "nickname") String nickname) {
		return ResponseEntity.ok(userService.checkNickname(nickname));
	}

	@Operation(summary = "내 정보 조회")
	@GetMapping("/me")
	public ResponseEntity<UserInfoDto> getMyInfo(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		UserInfoDto dto = userService.getUser(userDetails.getUsername());
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "프로필 사진 변경")
	@PutMapping("/profile-image")
	public ResponseEntity<UserDto> updateProfileImage(Authentication authentication,
		@RequestParam("file") MultipartFile file) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		UserDto userDto = userService.updateProfileImage(userDetails.getUsername(), file);
		return ResponseEntity.ok(userDto);
	}

	// @Operation(summary = "내 정보 수정")
	// @PutMapping("/info/edit")
	// public ResponseEntity<UserDto> editUserInfo(Authentication authentication,
	// 	@RequestPart(required = false) @Valid EditUserRequest data,
	// 	@RequestPart(value = "file", required = false) MultipartFile file) {
	// 	UserDetails userDetails = (UserDetails)authentication.getPrincipal();
	// 	UserDto dto = userService.edit(userDetails.getUsername(), data, file);
	// 	return ResponseEntity.ok(dto);
	// }

	// @Operation(summary = "회원 탈퇴")
	// @DeleteMapping
	// public ResponseEntity<StringResDto> deleteUser(Authentication authentication) {
	// 	UserDetails userDetails = (UserDetails)authentication.getPrincipal();
	// 	userService.delete(userDetails.getUsername());
	// 	return ResponseEntity.ok(new StringResDto("회원 탈퇴 성공"));
	// }

	// 이미지 관련
	// @Operation(summary = "이미지 URL")
	// @GetMapping("/images/profile/{id}/{filename}")
	// public ResponseEntity<Resource> getProfileImages(
	// 	@PathVariable String id,
	// 	@PathVariable String filename) {
	// 	return imagesService.getProfileImages(id, filename);
	// }

	// @PutMapping("/profile-image")
	// public ResponseEntity<StringResDto> updateProfileImage(
	// 	Authentication authentication,
	// 	@RequestParam("file") String file) {
	// 	UserDetails userDetails = (UserDetails)authentication.getPrincipal();
	// 	// userService.updateProfileImage(userDetails.getUsername(), file);
	// 	return ResponseEntity.ok(new StringResDto("프로필 이미지 변경 성공"));
	// }

	// @Operation(summary = "유저 정보 조회",
	// 	responses =
	// 		{
	// 			@ApiResponse(
	// 				responseCode = "200",
	// 				description = "조회 성공",
	// 				content = @Content(
	// 					mediaType = MediaType.APPLICATION_JSON_VALUE,
	// 					schema = @Schema(implementation = UserDto.class)
	// 				)
	// 			)
	// 		}
	// )
	// @GetMapping("/info/{id}")
	// public ResponseEntity<UserDto> getUser(@PathVariable String id) {
	// 	UserDto dto = userService.getUser(id);
	// 	return ResponseEntity.ok(dto);
	// }

	// 유저 검색 및 닉네임 중복 체크
	// @Operation(summary = "유저 검색(nickname)",
	// 	responses =
	// 		{
	// 			@ApiResponse(
	// 				responseCode = "200",
	// 				description = "유저 검색 성공",
	// 				content = @Content(
	// 					mediaType = MediaType.APPLICATION_JSON_VALUE,
	// 					schema = @Schema(implementation = UserDto.class)
	// 				)
	// 			)
	// 		}
	// )
	// @GetMapping
	// public ResponseEntity<UserDto> findByNickname(@RequestParam(value = "nickname") String nickname) {
	// 	UserDto dto = userService.findByNickname(nickname);
	// 	return ResponseEntity.ok(dto);
	// }
}




