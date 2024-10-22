package com.techwave.olol.mission.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.techwave.olol.mission.dto.MissionResDto;
import com.techwave.olol.mission.dto.request.ReqMissionDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionAdminService {
	public List<MissionResDto> getAllMissions() {
		return null;
	}

	public void createMission(ReqMissionDto reqMissionDto) {
	}

	public void deleteMission(UUID missionId) {
	}

	public void updateMission(ReqMissionDto reqMissionDto, UUID missionId) {
	}
}
