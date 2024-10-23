package com.techwave.olol.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "가족 존재 여부 response")
@AllArgsConstructor
@NoArgsConstructor
public class FamilyExistDto {
	@Schema(example = "false")
	private boolean exist = false;

	@Schema(example = "1L")
	private Long familyId;
}
