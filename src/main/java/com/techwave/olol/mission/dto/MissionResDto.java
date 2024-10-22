package com.techwave.olol.mission.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "미션 응답 dto")
public class MissionResDto {
	@Schema(description = "미션 ID", example = "1L")
	private Long missionId;
	@Schema(description = "미션 이름", example = "미션1")
	private String missionName;

	@Schema(description = "미션 설명", example = "미션 설명")
	private String missionDescription;
	@Schema(description = "미션 포인트", example = "100")
	private Long missionPoint;
	@Schema(description = "미션 날짜", example = "2021-01-01")
	private LocalDate missionDate;
	@Schema(description = "미션 수행 여부", example = "false")
	private boolean isComplete;

}
