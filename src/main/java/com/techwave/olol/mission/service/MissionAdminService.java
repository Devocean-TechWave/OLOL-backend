package com.techwave.olol.mission.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.dto.MissionResDto;
import com.techwave.olol.mission.dto.request.ReqMissionDto;
import com.techwave.olol.mission.exception.MissionErrorCode;
import com.techwave.olol.mission.exception.MissionException;
import com.techwave.olol.mission.repository.MissionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionAdminService {

	private final MissionRepository missionRepository;

	public List<MissionResDto> getAllMissions() {
		List<Mission> all = missionRepository.findAll();
		List<MissionResDto> missionResDtos = new ArrayList<>();
		for (Mission mission : all) {
			missionResDtos.add(MissionResDto.fromEntity(mission));
		}
		return missionResDtos;
	}

	public MissionResDto createMission(ReqMissionDto reqMissionDto) {
		Mission mission = reqMissionDto.toEntity();
		Mission savedMission = missionRepository.save(mission);
		return MissionResDto.fromEntity(savedMission);
	}

	@Transactional
	public void deleteMission(UUID missionId) {
		missionRepository.deleteMissionById(missionId);
	}

	@Transactional
	public MissionResDto updateMission(ReqMissionDto reqMissionDto, UUID missionId) {
		Mission mission = missionRepository.findById(missionId)
			.orElseThrow(() -> new MissionException(MissionErrorCode.MISSION_NOT_FOUND));
		mission.updateByReqDto(reqMissionDto);
		return MissionResDto.fromEntity(mission);
	}
}
