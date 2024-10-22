package com.techwave.olol.family.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.family.dto.FamilyExistDto;
import com.techwave.olol.family.dto.FamilyInfoDto;
import com.techwave.olol.family.dto.FamilyReqDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Family", description = "가족 정보 조회 및 수정 API")
@RestController
@RequestMapping("/api/v1/family")
@RequiredArgsConstructor
public class FamilyController {

	@Operation(summary = "가족 존재 여부 조회", description = "가족이 존재하는지 조회합니다.")
	@GetMapping("/exist")
	public FamilyExistDto exist(@RequestParam(value = "familyName") String familyName) {
		FamilyExistDto dto = new FamilyExistDto();
		dto.setExist(true);
		return dto;
	}

	@Operation(summary = "가족 생성", description = "가족을 생성합니다.")
	@PostMapping("/create")
	public FamilyExistDto create(@RequestBody FamilyReqDto reqDto) {
		FamilyExistDto dto = new FamilyExistDto();
		dto.setExist(true);
		return dto;
	}

	@Operation(summary = "가족 가입", description = "가족에 가입합니다.")
	@PostMapping("/join")
	public FamilyExistDto join(@RequestBody FamilyReqDto reqDto) {
		FamilyExistDto dto = new FamilyExistDto();
		dto.setExist(true);
		return dto;
	}

	@Operation(summary = "내 가족 정보 조회", description = "내 가족 정보를 조회합니다.")
	@GetMapping("/me")
	public FamilyInfoDto getMyFamilyInfo() {
		FamilyInfoDto dto = new FamilyInfoDto();
		dto.setFamilyName("홍길동 가족");
		return dto;
	}

	@Operation(summary = "가족 랭킹 조회", description = "가족 랭킹을 조회합니다.")
	@GetMapping("/rank")
	public List<FamilyInfoDto> getFamilyRank() {
		return null;
	}
}
