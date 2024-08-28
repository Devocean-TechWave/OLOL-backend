package com.techwave.olol.email.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
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