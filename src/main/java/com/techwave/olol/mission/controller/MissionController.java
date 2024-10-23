package com.techwave.olol.mission.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techwave.olol.global.dto.StringResDto;
import com.techwave.olol.mission.dto.MemoryResDto;
import com.techwave.olol.mission.dto.MissionResDto;
import com.techwave.olol.mission.service.MissionService;
import com.techwave.olol.user.dto.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/missons")
@Tag(name = "Mission", description = "미션 정보 조회 및 수정 API")
public class MissionController {

	private final MissionService missionService;

	@Operation(summary = "오늘 수행 중인 미션 조회", description = "오늘에 해당하는 미션을 조회합니다. 이미 미션이 끝났다면 조회되지 않습니다.")
	@GetMapping("/today")
	public ResponseEntity<MissionResDto> getProgressMission(@AuthenticationPrincipal SecurityUser user) {
		return ResponseEntity.ok(missionService.getTodayMission(user.getUsername()));
	}

	// 미션 인증
	@Operation(summary = "미션 인증", description = "미션을 인증합니다.")
	@PostMapping("/verification/{missionId}")
	public ResponseEntity<StringResDto> verifyMission(@AuthenticationPrincipal SecurityUser user,
		@PathVariable("missionId") UUID missionId,
		@RequestParam("file") MultipartFile file) {
		try {
			missionService.verifyMission(user.getUsername(), missionId, file);
			return ResponseEntity.ok(new StringResDto("미션이 인증되었습니다."));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StringResDto("미션 인증에 실패했습니다: " + e.getMessage()));
		}
	}

	// 미션 조회
	@Operation(summary = "테마 사진 생성", description = "테마 사진을 생성합니다.")
	@PostMapping("/theme")
	public ResponseEntity<StringResDto> createTheme(@RequestParam("file") String imageUrl) {
		try {
			missionService.createTheme(imageUrl);
			return ResponseEntity.ok(new StringResDto("테마 사진이 생성되었습니다."));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StringResDto("테마 사진 생성에 실패했습니다: " + e.getMessage()));
		}
	}

	@Operation(summary = "성공한 기록 조회", description = "성공한 미션을 조회합니다.")
	@GetMapping("/me/success")
	public ResponseEntity<List<MemoryResDto>> getSuccessMission(@AuthenticationPrincipal SecurityUser user,
		@RequestParam("all") boolean all) {
		return ResponseEntity.ok(missionService.getSuccessMission(user.getUsername(), all));
	}

	@Operation(summary = "가족이 성공한 기록 조회", description = "가족이 성공한 미션을 조회합니다.")
	@GetMapping("/family/success")
	public ResponseEntity<List<MemoryResDto>> getFamilySuccessMission(@AuthenticationPrincipal SecurityUser user,
		@RequestParam("familyId") Long familyId) {
		return ResponseEntity.ok(missionService.getSuccessMission(user.getUsername(), familyId));
	}
}
