package com.techwave.olol.notification.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.techwave.olol.mission.repository.MissionRepository;
import com.techwave.olol.notification.client.OneSignalClient;
import com.techwave.olol.notification.config.OneSignalProperties;
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
	private final UserRepository userRepository;
	private final MissionRepository missionRepository;

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

}


