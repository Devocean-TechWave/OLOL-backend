package com.techwave.olol.email.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;
import com.techwave.olol.email.domain.EmailReservation;
import com.techwave.olol.email.dto.EmailDto;
import com.techwave.olol.email.repository.EmailReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailReservationService {

	private final EmailReservationRepository emailReservationRepository;

	public EmailReservation saveEmail(String email) {
		if (emailReservationRepository.findByEmail(email).isPresent()) {
			throw new AuthException(AuthErrorCode.EMAIL_DUPLICATION);
		}
		EmailReservation emailReservation = new EmailReservation();
		emailReservation.setEmail(email);
		return emailReservationRepository.save(emailReservation);
	}

	public List<EmailDto> getAllEmails() {
		List<EmailReservation> all = emailReservationRepository.findAll();
		return EmailDto.from(all);
	}
}