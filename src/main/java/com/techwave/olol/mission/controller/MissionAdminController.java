package com.techwave.olol.mission.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.global.dto.StringResDto;
import com.techwave.olol.mission.dto.MissionResDto;
import com.techwave.olol.mission.dto.request.ReqMissionDto;
import com.techwave.olol.mission.service.MissionAdminService;
import com.techwave.olol.user.dto.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mission Admin", description = "미션 정보 조회 및 수정 API(관리자용)")
@RequestMapping("/api/v1/admin/missions")
public class MissionAdminController {

	private final MissionAdminService missionService;

	@Operation(summary = "미션 조회(관리자용)", description = "모든 미션을 조회합니다.")
	@GetMapping
	public ResponseEntity<List<MissionResDto>> getMissions(@AuthenticationPrincipal SecurityUser user) {
		return ResponseEntity.ok(missionService.getAllMissions());
	}

	// 미션 등록
	@Operation(summary = "미션 등록(관리자용)", description = "미션을 만들어서 등록합니다.")
	@PostMapping
	public ResponseEntity<MissionResDto> registerMission(@AuthenticationPrincipal SecurityUser user,
		@RequestBody ReqMissionDto reqMissionDto) {
		return ResponseEntity.ok(missionService.createMission(reqMissionDto));
	}

	// 미션 삭제
	@Operation(summary = "미션 삭제(관리자용)", description = "미션을 삭제합니다.")
	@DeleteMapping("/{missionId}")
	public ResponseEntity<StringResDto> deleteMission(@AuthenticationPrincipal SecurityUser user,
		@PathVariable UUID missionId) {
		missionService.deleteMission(missionId);
		return ResponseEntity.ok(new StringResDto("미션이 삭제되었습니다."));
	}

	@Operation(summary = "미션 수정(관리자용)", description = "미션을 수정합니다.")
	@PutMapping("/update/{missionId}")
	public ResponseEntity<MissionResDto> updateMission(@AuthenticationPrincipal SecurityUser user,
		@PathVariable UUID missionId, @RequestBody ReqMissionDto reqMissionDto) {
		return ResponseEntity.ok(missionService.updateMission(reqMissionDto, missionId));
	}

}
