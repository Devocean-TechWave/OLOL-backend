package com.techwave.olol.mission.controller;

import java.util.List;
import java.util.UUID;

import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.dto.request.ReqMissionDto;
import com.techwave.olol.mission.service.MissionService;
import com.techwave.olol.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/missons")
public class MissionController {

	private final MissionService missionService;

	// 미션 등록
	@PostMapping
	public ResponseEntity<String> registerMission(@AuthenticationPrincipal User user,
		@RequestBody ReqMissionDto reqMissionDto) {

		missionService.registerMission(user.getNickname(), reqMissionDto);

		return ResponseEntity.ok("미션이 생성되었습니다.");
	}

	// TODO: 미션 인증

	// 미션 조회
	@GetMapping("/received/{userId}")
	public ResponseEntity<?> getProgressMission(@AuthenticationPrincipal User user,
		@PathVariable UUID userId,
		@RequestParam boolean active) {

		List<Mission> missions = missionService.getMissions(user.getNickname(), active, false);
		return ResponseEntity.ok(missions);
	}

	@GetMapping("/given/{userId}")
	public ResponseEntity<?> getGivenMissions(@AuthenticationPrincipal User user,
		@PathVariable UUID userId,
		@RequestParam boolean active) {
		List<Mission> missions = missionService.getMissions(user.getNickname(), active, true);
		return ResponseEntity.ok(missions);
	}
}
