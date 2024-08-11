package com.techwave.olol.mission.service;

import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.dto.request.ReqMissionDto;
import com.techwave.olol.mission.repository.MissionRepository;
import com.techwave.olol.mission.repository.SuccessStampRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionService {

	private final MissionRepository missionRepository;
	private final UserRepository userRepository;

	// 미션 조회
	public List<Mission> getMissions(String userNickname, boolean active, boolean isGiver) {
		// 유저 조회
		User user = userRepository.findByNickname(userNickname)
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + userNickname));

		// 유저가 생성한 모든 미션 조회
		if (isGiver) {
			return active
				? missionRepository.findProgressMissionsByGiver(user)
				: missionRepository.findCompletedMissionsByGiver(user);
		} else {
			return active
				? missionRepository.findProgressMissionsByReceiver(user)
				: missionRepository.findCompletedMissionsByReceiver(user);
		}

	}

	public void registerMission(String userNickname, ReqMissionDto reqMissionDto) {
		// 유저 조회
		User giver = userRepository.findByNickname(userNickname)
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + userNickname));

		// 미션 유저 조회
		User receiver = userRepository.findByNickname(reqMissionDto.getReceiverId())
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + reqMissionDto.getReceiverId()));
		// 미션 작성 가능성 validate
		checkMission(reqMissionDto);

		// 미션 등록
		Mission mission = Mission.createMission(reqMissionDto);
		mission.setGiver(giver);
		mission.setReceiver(receiver);

		// 미션 저장
		missionRepository.save(mission);
	}

	// === 편의 메서드 ===
	private void checkMission(ReqMissionDto reqMissionDto) {
		// 미션 기간 확인
		checkMissionWeek(reqMissionDto.getStartAt(), reqMissionDto.getEndAt());

		// 미션 성공 횟수 확인
		if (reqMissionDto.getSuccessQuota() <= 0) {
			throw new IllegalArgumentException("미션 성공 횟수는 0보다 커야합니다.");
		}
	}

	private void checkMissionWeek(LocalDate startDate, LocalDate endDate) {
		LocalDate now = LocalDate.now();
		if (startDate.isBefore(now.minusWeeks(1))) {
			throw new IllegalArgumentException("미션 주차는 현재 주차보다 이전일 수 없습니다.");
		}
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("미션 시작일은 미션 종료일보다 늦을 수 없습니다.");
		}
	}
}
