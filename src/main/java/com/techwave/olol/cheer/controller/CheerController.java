package com.techwave.olol.cheer.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.cheer.dto.CheerRequestDto;
import com.techwave.olol.cheer.service.CheerService;
import com.techwave.olol.global.util.SecurityUtil;
import com.techwave.olol.user.dto.UserInfoDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cheers")
public class CheerController {
	private final CheerService cheerService;

	@Operation(summary = "응원 하기", description = "다른 유저에게 응원을 보냅니다.")
	@PostMapping("/{missionId}")
	public ResponseEntity<UserInfoDto> cheers(@PathVariable UUID missionId,
		@RequestBody CheerRequestDto cheerRequestDto) {
		return ResponseEntity.ok(
			cheerService.cheers(SecurityUtil.getCurrentUserId(), missionId, cheerRequestDto.getCheerType()));
	}

	@Operation(summary = "응원 요청", description = "다른 친구에게 응원을 요청합니다.")
	@PostMapping("/request/{userId}")
	public ResponseEntity<UserInfoDto> request(@PathVariable String userId) {
		return ResponseEntity.ok(cheerService.request(SecurityUtil.getCurrentUserId(), userId));
	}

}
