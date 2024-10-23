package com.techwave.olol.mission.dto.request;

import java.time.LocalDate;

import com.techwave.olol.mission.domain.Mission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqMissionDto {
	@Schema(description = "미션 이름", example = "미션1")
	private String name;
	@Schema(description = "미션 설명", example = "미션 설명")
	private String description;
	@Schema(description = "미션 날짜", example = "2021-01-01")
	private LocalDate date;
	@Schema(description = "미션 포인트", example = "100")
	private Long point;

	public Mission toEntity() {
		return Mission.builder().name(name).description(description).date(date).point(point).build();
	}
}
