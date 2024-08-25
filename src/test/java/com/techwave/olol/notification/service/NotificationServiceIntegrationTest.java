package com.techwave.olol.notification.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.techwave.olol.notification.dto.NotificationResDto;
import com.techwave.olol.notification.dto.NotificationType;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql(scripts = {"/set-all-relation.sql"})
@DisplayName("알림 서비스 통합 테스트")
public class NotificationServiceIntegrationTest {

	@Autowired
	private NotificationService notificationService;

	@BeforeEach
	public void setup() {
		// 테스트 전 데이터 초기화 또는 준비 작업
	}

	// @Test
	// @DisplayName("사용자 알림 조회 - 친구 요청, 수락, 거절, 응원, poke 알림")
	// @Transactional
	// public void testGetNotifications() throws Exception {
	// 	// GIVEN: 사용자 ID "1"이 주어졌을 때
	// 	String userId = "1";
	//
	// 	// WHEN: 알림 조회 메서드 호출
	// 	List<NotificationResDto> notifications = notificationService.getNotification(userId);
	//
	// 	// THEN: 반환된 알림 목록이 올바른지 확인
	// 	assertThat(notifications).isNotEmpty();
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.FRIEND_REQUEST);
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.ACCEPT_FRIEND_REQUEST);
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.REJECT_FRIEND_REQUEST);
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.LIKE);
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.POKE);
	// }

	@Test
	@DisplayName("친구 요청 수락 알림 조회")
	@Transactional
	public void testGetNotificationsForAcceptedFriendRequests() throws Exception {
		// GIVEN: 사용자 ID "3"이 주어졌을 때
		String userId = "3";

		// WHEN: 알림 조회 메서드 호출
		List<NotificationResDto> notifications = notificationService.getNotification(userId);

		// THEN: 수락된 친구 요청 알림이 포함되어 있는지 확인
		assertThat(notifications).isNotEmpty();
		assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.ACCEPT_FRIEND_REQUEST);
	}

	@Test
	@DisplayName("친구 요청 거절 알림 조회")
	@Transactional
	public void testGetNotificationsForRejectedFriendRequests() throws Exception {
		// GIVEN: 사용자 ID "4"가 주어졌을 때
		String userId = "4";

		// WHEN: 알림 조회 메서드 호출
		List<NotificationResDto> notifications = notificationService.getNotification(userId);

		// THEN: 거절된 친구 요청 알림이 포함되어 있는지 확인
		assertThat(notifications).isNotEmpty();
		assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.REJECT_FRIEND_REQUEST);
	}
	//
	// @Test
	// @DisplayName("응원 알림 조회")
	// @Transactional
	// public void testGetNotificationsForCheers() throws Exception {
	// 	// GIVEN: 사용자 ID "3"이 주어졌을 때
	// 	String userId = "3";
	//
	// 	// WHEN: 알림 조회 메서드 호출
	// 	List<NotificationResDto> notifications = notificationService.getNotification(userId);
	//
	// 	// THEN: 응원 알림이 포함되어 있는지 확인
	// 	assertThat(notifications).isNotEmpty();
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.LIKE);
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.LOVE);
	// }

	// @Test
	// @DisplayName("Poke 알림 조회")
	// @Transactional
	// public void testGetNotificationsForPokes() throws Exception {
	// 	// GIVEN: 사용자 ID "1"이 주어졌을 때
	// 	String userId = "1";
	//
	// 	// WHEN: 알림 조회 메서드 호출
	// 	List<NotificationResDto> notifications = notificationService.getNotification(userId);
	//
	// 	// THEN: poke 알림이 포함되어 있는지 확인
	// 	assertThat(notifications).isNotEmpty();
	// 	assertThat(notifications).anyMatch(n -> n.getType() == NotificationType.POKE);
	// }

	// @Test
	// @DisplayName("친구 요청이 없는 사용자 알림 조회")
	// @Transactional
	// public void testGetNotificationsNoFriendRequests() throws Exception {
	// 	// GIVEN: 친구 요청이 없는 사용자 ID "5"가 주어졌을 때
	// 	String userId = "5";
	//
	// 	// WHEN: 알림 조회 메서드 호출
	// 	List<NotificationResDto> notifications = notificationService.getNotification(userId);
	//
	// 	// THEN: 반환된 알림 목록이 비어 있는지 확인
	// 	assertThat(notifications).isEmpty();
	// }

}
