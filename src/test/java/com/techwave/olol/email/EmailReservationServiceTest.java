package com.techwave.olol.email;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.techwave.olol.email.domain.EmailReservation;
import com.techwave.olol.email.dto.EmailDto;
import com.techwave.olol.email.service.EmailReservationService;

@SpringBootTest
@EnableJpaAuditing
class EmailReservationServiceTest {
	@Autowired
	private EmailReservationService emailReservationService;

	@Test
	@DisplayName("이메일 저장 테스트")
	void testSaveEmail() {
		EmailReservation emailReservation = emailReservationService.saveEmail("wnddms12345@naver.com");
		assertNotNull(emailReservation);

	}

	@Test
	@DisplayName("이메일 중복 저장 실패 테스트")
	void testSaveEmailFail2() {
		emailReservationService.saveEmail("wnddms12345@gmail.com");
		assertThrows(Exception.class, () -> {
			emailReservationService.saveEmail("wnddms12345@gmail.com");
		});
	}

	@Test
	@DisplayName("이메일 목록 가져오기 테스트")
	void testGetAllEmails() {
		emailReservationService.saveEmail("wnddms1234@gmail.com");
		emailReservationService.saveEmail("wnddms1234@gmai2l.com");
		emailReservationService.saveEmail("wnddms1234@gmail3.com");

		List<EmailDto> allEmails = emailReservationService.getAllEmails();

		assertNotNull(allEmails.get(0).getCreatedAt());
	}

}