package com.techwave.olol.mission.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.dto.request.ReqMissionDto;
import com.techwave.olol.mission.service.MissionService;
import com.techwave.olol.user.dto.SecurityUser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/missons")
public class MissionController {

	private final MissionService missionService;

	// 미션 등록
	@PostMapping
	public ResponseEntity<String> registerMission(@AuthenticationPrincipal SecurityUser user,
		@RequestBody ReqMissionDto reqMissionDto) {

		missionService.registerMission(user.getUsername(), reqMissionDto);

		return ResponseEntity.ok("미션이 생성되었습니다.");
	}

	// 미션 인증
	@PostMapping("/verification/{missionId}")
	public ResponseEntity<String> verifyMission(@AuthenticationPrincipal SecurityUser user,
		@PathVariable("missionId") UUID missionId,
		@RequestParam("file") MultipartFile file) {
		try {
			missionService.verifyMission(user.getUsername(), missionId, file);
			return ResponseEntity.ok("미션이 인증되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("미션 인증에 실패했습니다: " + e.getMessage());
		}
	}

	// 미션 조회
	@GetMapping("/received/{userId}")
	public ResponseEntity<?> getProgressMission(@AuthenticationPrincipal SecurityUser user,
		@PathVariable("userId") UUID userId,
		@RequestParam boolean active) {

		List<Mission> missions = missionService.getMissions(user.getUsername(), active, false);
		return ResponseEntity.ok(missions);
	}

	@GetMapping("/given/{userId}")
	public ResponseEntity<?> getGivenMissions(@AuthenticationPrincipal SecurityUser user,
		@PathVariable UUID userId,
		@RequestParam boolean active) {
		List<Mission> missions = missionService.getMissions(user.getUsername(), active, true);
		return ResponseEntity.ok(missions);
	}

	// 미션 삭제
	@DeleteMapping("/delete/{missionId}")
	public ResponseEntity<String> deleteMission(@AuthenticationPrincipal SecurityUser user,
		@PathVariable UUID missionId) {
		missionService.deleteMission(user.getUsername(), missionId);
		return ResponseEntity.ok("미션이 삭제되었습니다.");
	}
}
