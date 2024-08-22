package com.techwave.olol.mission.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.dto.GivenMissionDto;
import com.techwave.olol.mission.dto.ReceivedMissionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespMissionDto {

	private int missionCnt;
	private List<ReceivedMissionDto> receivedMissions;
	private List<GivenMissionDto> givenMissions;

	public static RespMissionDto createRespMissionDto(int missionCnt, List<Mission> receivedMissions,
		List<Mission> givenMissions) {
		List<ReceivedMissionDto> receivedMissionDtoList = receivedMissions.stream()
			.map(mission -> new ReceivedMissionDto(mission, mission.getEmoji(), mission.getSuccessQuota(),
				mission.getReceiver().getName(), mission.getSuccessStamps().size()))
			.collect(Collectors.toList());

		List<GivenMissionDto> givenMissionDtoList = givenMissions.stream()
			.map(mission -> new GivenMissionDto(mission, mission.getEmoji(), mission.getSuccessQuota(),
				mission.getGiver().getName(), mission.getSuccessStamps().size()))
			.collect(Collectors.toList());

		return RespMissionDto.builder()
			.missionCnt(missionCnt)
			.receivedMissions(receivedMissionDtoList)
			.givenMissions(givenMissionDtoList)
			.build();
	}
}