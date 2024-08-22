package com.techwave.olol.notification.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techwave.olol.notification.client.OneSignalClient;
import com.techwave.olol.notification.config.OneSignalProperties;

@DisplayName("NotificationService 모킹 테스트")
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@Mock
	private OneSignalClient oneSignalClient;

	@Mock
	private OneSignalProperties oneSignalProperties;

	@InjectMocks
	private NotificationService notificationService;

	@Test
	@DisplayName("알림 메시지 전송 테스트")
	void sendNotification() {
		// given
		String userId = "d7b57432-cb5b-4b7e-991c-833ea50f19f1"; // 내 로컬 디바이스 아이디
		String title = "알림 제목";
		String message = "알림 내용";
		String mockResponse = "Success";

		// OneSignalProperties의 반환값 모킹
		when(oneSignalProperties.getAppId()).thenReturn("mock-app-id");
		when(oneSignalProperties.getRestApiKey()).thenReturn("mock-rest-api-key");

		// OneSignalClient의 sendNotification 메서드 모킹
		when(oneSignalClient.sendNotification(anyString(), anyMap())).thenReturn(mockResponse);

		// when
		notificationService.sendNotification(userId, title, message);

		// then
		verify(oneSignalClient, times(1)).sendNotification(
			eq("Basic mock-rest-api-key"), anyMap());
	}
}
