package com.techwave.olol.mission.dto;

import java.util.UUID;

import com.techwave.olol.mission.domain.Mission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GivenMissionDto {
	private final UUID id;
	private final String name;
	private final String emoji;
	private final int successQuota;
	private final String userName;
	private final int successStampCount;

	public GivenMissionDto(Mission mission, String emoji, int successQuota, String userName, int successStampCount) {
		this.id = mission.getId();
		this.name = mission.getName();
		this.emoji = emoji;
		this.successQuota = successQuota;
		this.userName = mission.getGiver().getName();
		this.successStampCount = mission.getSuccessStamps().size();
	}
}
