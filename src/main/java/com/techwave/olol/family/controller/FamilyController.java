package com.techwave.olol.family.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.family.dto.FamilyExistDto;
import com.techwave.olol.family.dto.FamilyInfoDto;
import com.techwave.olol.family.dto.FamilyReqDto;
import com.techwave.olol.family.service.FamilyService;
import com.techwave.olol.user.dto.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Family", description = "가족 정보 조회 및 수정 API")
@RestController
@RequestMapping("/api/v1/family")
@RequiredArgsConstructor
public class FamilyController {

	private final FamilyService familyService;

	@Operation(summary = "가족 존재 여부 조회", description = "가족이 존재하는지 조회합니다.")
	@GetMapping("/exist")
	public FamilyExistDto exist(@RequestParam(value = "familyName") String familyName) {
		return familyService.existFamily(familyName);
	}

	@Operation(summary = "가족 생성", description = "가족을 생성합니다.")
	@PostMapping("/create")
	public FamilyInfoDto create(@AuthenticationPrincipal SecurityUser user,
		@RequestBody FamilyReqDto familyReqDto) {
		return familyService.createFamily(user.getUsername(), familyReqDto);
	}

	@Operation(summary = "가족 가입", description = "가족에 가입합니다.")
	@PostMapping("/join")
	public FamilyInfoDto join(@AuthenticationPrincipal SecurityUser user, @RequestBody FamilyReqDto reqDto) {
		return familyService.joinFamily(user.getUsername(), reqDto);
	}

	@Operation(summary = "내 가족 정보 조회", description = "내 가족 정보를 조회합니다.")
	@GetMapping("/me")
	public FamilyInfoDto getMyFamilyInfo(@AuthenticationPrincipal SecurityUser user) {
		return familyService.getFamilyInfo(user.getUsername());
	}

	@Operation(summary = "가족 랭킹 조회", description = "가족 랭킹을 조회합니다.")
	@GetMapping("/rank")
	public List<FamilyInfoDto> getFamilyRank() {
		return familyService.getFamilyRank();
	}
}
