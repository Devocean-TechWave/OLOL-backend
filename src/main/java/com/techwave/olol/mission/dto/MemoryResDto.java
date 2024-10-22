package com.techwave.olol.mission.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "미션 응답")
public class MemoryResDto {
	@Schema(example = "xxxx-xxxx-xxxx-xxxx")
	UUID missionId;
	@Schema(example = "미션1")
	String missionName;
	@Schema(example = "1L")
	Long memoryId;
	@Schema(example = "https://techwave.com/olol/image/xxxx-xxxx-xxxx-xxxx.jpg")
	String imageUrl;
	@Schema(example = "https://techwave.com/olol/image/xxxx-xxxx-xxxx-xxxx.jpg")
	String createdUrl;
	@Schema(example = "2021-01-01-01T01:01:01")
	LocalDateTime createdTime;
	@Schema(example = "100L")
	Long score;
}
