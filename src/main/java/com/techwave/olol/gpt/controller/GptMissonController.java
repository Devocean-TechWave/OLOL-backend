package com.techwave.olol.gpt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.gpt.dto.GptReqDto;
import com.techwave.olol.gpt.dto.GptResDto;
import com.techwave.olol.gpt.service.GptMissonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "GPT Mission", description = "CHAT GPT 테스트")
@RequestMapping("/api/v1/admin/gptmissions")
@RequiredArgsConstructor
public class GptMissonController {
	private final GptMissonService gptMissionService;

	@Operation(summary = "GPT 채팅 생성", description = "카테고리에 해당하는 프롬프트로 채팅을 생성합니다.")
	@PostMapping("/get_mission")
	public ResponseEntity<GptResDto> getMission(@RequestBody GptReqDto request) {
		// 카테고리에 맞는 프롬프트 생성
		String prompt = createPromptForMission(request.getCategory());

		try {
			// GPT API 호출
			String mission = gptMissionService.callGptApi(prompt);

			// 응답 생성
			GptResDto response = GptResDto.builder()
					.mission(mission)
					.build();

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
				.body(GptResDto.builder()
						.error("Failed to fetch mission")
						.build());
		}
	}

	// 카테고리별 프롬프트 생성
	private String createPromptForMission(String category) {
		if ("일상".equals(category)) {
			return "가족이 함께할 수 있는 일상적인 미션을 추천해줘. 이 미션은 하루동안 자연스럽게 수행할 수 있는 내용이여야 해. 여행이나 운동과 관련된 미션은 제외하고, 미션을 한줄로 요약해줘.";
		} else if ("운동".equals(category)) {
			return "가족이 함께 할 수 있는 운동 미션을 추천해줘. 이 미션은 하루동안 자연스럽게 수행할 수 있는 내용이여야 해. 일상적인 일이나 여행과 관련된 미션은 제외하고, 미션을 한줄로 요약해줘.";
		} else if ("여행".equals(category)) {
			return "가족이 여행 중에 함께 할 수 있는 미션을 추천해줘.일상적인 일이나 운동과 관련된 미션은 제외하고, 미션을 한줄로 요약해줘.";
		} else {
			return "가족들간 서로 감사를 표현할 수 있는 간단한 미션을 추천해줘. 이 미션은 하루동안 자연스럽게 수행할 수 있는 내용이여야 하고, 미션을 한줄로 요약해줘.";
		}
	}


	// GPT API 호출 함수 (HTTP 요청 등 구현)
	private String callGptApi(String prompt) throws Exception {
		// GPT API 호출 로직 구현
		// 여기서는 간단히 설명하기 위해 가상의 호출로 대체
		return "추천된 미션 예시";
	}
}