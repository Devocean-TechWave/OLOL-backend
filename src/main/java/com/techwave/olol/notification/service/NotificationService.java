package com.techwave.olol.notification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.techwave.olol.cheer.domain.Cheer;
import com.techwave.olol.cheer.domain.CheerType;
import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.repository.MissionRepository;
import com.techwave.olol.notification.client.OneSignalClient;
import com.techwave.olol.notification.config.OneSignalProperties;
import com.techwave.olol.notification.domain.Poke;
import com.techwave.olol.notification.dto.NotificationResDto;
import com.techwave.olol.notification.dto.NotificationType;
import com.techwave.olol.relation.domain.RelationStatus;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.relation.repository.UserRelationShipRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final OneSignalProperties oneSignalProperties;
	private final OneSignalClient oneSignalClient;
	private final UserRelationShipRepository userRelationShipRepository;
	private final UserRepository userRepository;
	private final MissionRepository missionRepository;

	/**
	 * 알림 조회 -> 알림 종류 : 응원, 친구 요청 (요청 미수락, 수락완료)
	 * @param userId : 사용자 ID
	 * @return 알림 목록
	 */
	//TODO: 쿼리 최적화 필요. 지금 난리남
	public List<NotificationResDto> getNotification(String userId) {
		List<UserRelationShip> receivedRelationNotAccepted = userRelationShipRepository.findTop10ByReceiverIdAndRelationStatus(
			userId, RelationStatus.REQUEST);//내게 온 친구 요청 목록
		List<UserRelationShip> sendRelationAccepted = userRelationShipRepository.findTop10BySenderIdAndRelationStatus(
			userId, RelationStatus.ACCEPT);//내가 보낸 친구 요청 목록 중 수락된 목록 업데이트 시간 기준으로 졍렬 됨
		List<UserRelationShip> sendRelationRejected = userRelationShipRepository.findTop10BySenderIdAndRelationStatus(
			userId, RelationStatus.REJECT);//내가 보낸 친구 요청 목록 중 거절된 목록 업데이트 시간 기준으로 정렬 됨
		List<Mission> missions = missionRepository.findByReceiverId(userId); //나의 미션 목록
		List<Cheer> allCheers = new ArrayList<>(); //내가 받은 모든 응원 목록
		for (Mission mission : missions) {
			allCheers.addAll(mission.getCheers());
		}

		List<UserRelationShip> friends = userRelationShipRepository.findAllAcceptedRelationByUserId(
			userId, RelationStatus.ACCEPT);//나와 친구 관계인 모든 사람.
		List<Poke> pokes = new ArrayList<>();
		for (UserRelationShip userRelationShip : friends) {
			pokes.addAll(userRelationShip.getPokes());
		}
		return setNotification(receivedRelationNotAccepted, sendRelationAccepted,
			sendRelationRejected, allCheers, pokes);
	}

	private List<NotificationResDto> setNotification(List<UserRelationShip> friendRequest,
		List<UserRelationShip> friendAccept, List<UserRelationShip> friendReject,
		List<Cheer> cheers, List<Poke> pokes) {
		List<NotificationResDto> notificationResDtos = new ArrayList<>();
		for (UserRelationShip userRelationShip : friendRequest) {
			notificationResDtos.add(NotificationResDto.builder()
				.type(NotificationType.FRIEND_REQUEST)
				.senderId(userRelationShip.getSender().getId())
				.senderName(userRelationShip.getSender().getName())
				.build());
		}

		for (UserRelationShip userRelationShip : friendAccept) {
			notificationResDtos.add(NotificationResDto.builder()
				.type(NotificationType.ACCEPT_FRIEND_REQUEST)
				.senderId(userRelationShip.getReceiver().getId())
				.senderName(userRelationShip.getReceiver().getName())
				.build());
		}

		for (UserRelationShip userRelationShip : friendReject) {
			notificationResDtos.add(NotificationResDto.builder()
				.type(NotificationType.REJECT_FRIEND_REQUEST)
				.senderId(userRelationShip.getReceiver().getId())
				.senderName(userRelationShip.getReceiver().getName())
				.build());
		}

		for (Cheer cheer : cheers) {
			NotificationType type;
			if (cheer.getCheerType() == CheerType.LIKE) {
				type = NotificationType.LIKE;
			} else if (cheer.getCheerType() == CheerType.LOVE) {
				type = NotificationType.LOVE;
			} else {
				type = NotificationType.CHEER;
			}
			notificationResDtos.add(NotificationResDto.builder()
				.type(type)
				.senderId(cheer.getGiver().getId())
				.senderName(cheer.getGiver().getName())
				.build());
		}

		for (Poke poke : pokes) {
			notificationResDtos.add(NotificationResDto.builder()
				.type(NotificationType.POKE)
				.senderId(poke.getUserRelationShip().getSender().getId())
				.senderName(poke.getUserRelationShip().getSender().getName())
				.build());
		}
		return notificationResDtos;
	}

	public void sendNotification(String userId, String title, String message) {
		Map<String, Object> notification = new HashMap<>();
		notification.put("app_id", oneSignalProperties.getAppId());
		notification.put("headings", Map.of("en", title));
		notification.put("contents", Map.of("en", message));
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
		);
		notification.put("include_player_ids", new String[] {user.getOneSignalId()}); // OneSignal에서 관리되는 사용자의 고유 ID
		// OneSignal 서버로 POST 요청
		String response = oneSignalClient.sendNotification(
			"Basic " + oneSignalProperties.getRestApiKey(),
			notification
		);
		log.info("OneSignal Response : {}", response);
	}

	//TODO: CheerType 프론트랑 맞춰서 메시지 수정해야함.
	public void sendNotification(Cheer cheer, User receiver) {
		String title = cheer.getGiver().getName() + "님이 응원했어요!";
		String message = cheer.getGiver().getName() + "님이 " + cheer.getCheerType().getName() + "로 응원했어요!";
		sendNotification(receiver.getId(), title, message);
	}

	//TODO: PokeType 프론트랑 맞춰서 메시지 수정해야함.
	public void sendNotification(Poke poke, String receiverId) {
		User receiver;
		UserRelationShip userRelationShip = poke.getUserRelationShip();
		if (userRelationShip.getSender().getId().equals(receiverId)) {
			receiver = userRelationShip.getSender();
		} else if (userRelationShip.getReceiver().getId().equals(receiverId)) {
			receiver = userRelationShip.getReceiver();
		} else {
			throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
		}
		String title = receiver.getName() + "님이 찔렀어요!";
		String message = receiver.getNickname() + "님이 찔렀어요!";
		sendNotification(receiver.getId(), title, message);
	}
}


