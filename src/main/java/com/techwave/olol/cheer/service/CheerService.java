package com.techwave.olol.cheer.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.techwave.olol.cheer.domain.Cheer;
import com.techwave.olol.cheer.domain.CheerType;
import com.techwave.olol.cheer.repository.CheerRepository;
import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.repository.MissionRepository;
import com.techwave.olol.notification.domain.Poke;
import com.techwave.olol.notification.repository.PokeRepository;
import com.techwave.olol.notification.service.NotificationService;
import com.techwave.olol.relation.domain.RelationStatus;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.relation.repository.UserRelationShipRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.UserInfoDto;
import com.techwave.olol.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheerService {
	private final CheerRepository cheerRepository;
	private final UserRepository userRepository;
	private final MissionRepository missionRepository;
	private final UserRelationShipRepository userRelationShipRepository;
	private final PokeRepository pokeRepository;
	private final NotificationService notificationService;

	/**
	 * 응원하기
	 * @param missionId 응원할 미션 아이디
	 * @param cheerType 응원 타입
	 * @return 응원한 유저 정보
	 */
	public UserInfoDto cheers(String userId, UUID missionId, CheerType cheerType) {
		Mission mission = missionRepository.findById(missionId)
			.orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));
		Cheer cheer = Cheer.builder()
			.cheerType(cheerType)
			.cheerGiver(
				userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다.")))
			.mission(mission)
			.build();
		cheerRepository.save(cheer);
		User receiver = mission.getReceiver();
		// notificationService.sendCheerNotification(cheer, receiver);
		return UserInfoDto.fromEntity(receiver);
	}

	/**
	 * 콕 찌르기
	 * @param giverId 콕 찌르는 유저 아이디
	 * @param receiverId 콕 찔리는 유저 아이디
	 * @return 콕 찔린 유저 정보
	 */
	public UserInfoDto request(String giverId, String receiverId) {
		UserRelationShip userRelationShip = userRelationShipRepository.findAcceptedRelationBetweenUsers(giverId,
			receiverId, RelationStatus.ACCEPT).orElseThrow(() -> new IllegalArgumentException("친구 관계가 아닙니다."));
		Poke poke = Poke.builder()
			.userRelationShip(userRelationShip)
			.date(LocalDate.now())
			.build();
		userRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("콕 찔린 유저를 찾을 수 없습니다."));
		pokeRepository.save(poke);
		//TODO: notificationService.sendPokeNotification(poke);
		return UserInfoDto.fromEntity(userRepository.findById(receiverId).get());
	}
}
