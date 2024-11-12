package com.techwave.olol.gpt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GptReqDto {
	@Schema(description = "카테고리", example = "일상")
	private String category;
}
