package com.techwave.olol.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "가족 생성 request")
public class FamilyReqDto {
	@Schema(description = "가족 이름", example = "홍길동 가족")
	private String familyName;

	@Schema(description = "내 역할", example = "아들")
	private String myRole;
}
