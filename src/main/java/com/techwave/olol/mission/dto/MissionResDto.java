package com.techwave.olol.mission.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.techwave.olol.mission.domain.Mission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "미션 응답 dto")
@Builder
@AllArgsConstructor
public class MissionResDto {
	@Schema(description = "미션 ID", example = "1L")
	private UUID missionId;
	@Schema(description = "미션 이름", example = "미션1")
	private String missionName;

	@Schema(description = "미션 설명", example = "미션 설명")
	private String missionDescription;
	@Schema(description = "미션 포인트", example = "100")
	private Long missionPoint;
	@Schema(description = "미션 날짜", example = "2021-01-01")
	private LocalDate missionDate;

	public static MissionResDto fromEntity(Mission mission) {
		return MissionResDto.builder().missionId(mission.getId()).missionName(mission.getName())
			.missionDescription(mission.getDescription()).missionPoint(mission.getPoint())
			.missionDate(mission.getDate()).build();
	}
}
