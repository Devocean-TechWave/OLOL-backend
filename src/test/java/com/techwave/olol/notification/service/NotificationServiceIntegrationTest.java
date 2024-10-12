package com.techwave.olol.notification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

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

}
