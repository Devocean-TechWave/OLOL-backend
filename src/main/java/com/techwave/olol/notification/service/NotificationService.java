package com.techwave.olol.notification.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.techwave.olol.notification.client.OneSignalClient;
import com.techwave.olol.notification.config.OneSignalProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final OneSignalProperties oneSignalProperties;
	private final OneSignalClient oneSignalClient;

	public void sendNotification(String userId, String title, String message) {
		Map<String, Object> notification = new HashMap<>();
		notification.put("app_id", oneSignalProperties.getAppId());
		notification.put("contents", Map.of("en", message));
		notification.put("headings", Map.of("en", title));
		notification.put("include_player_ids", new String[] {userId}); // OneSignal에서 관리되는 사용자의 고유 ID

		// OneSignal 서버로 POST 요청
		String response = oneSignalClient.sendNotification(
			"Basic " + oneSignalProperties.getRestApiKey(),
			notification
		);
		System.out.println("OneSignal Response: " + response);
	}
}


