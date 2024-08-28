package com.techwave.olol.email.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.email.domain.EmailReservation;
import com.techwave.olol.email.dto.EmailDto;
import com.techwave.olol.email.service.EmailReservationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailReservationController {
	private final EmailReservationService emailReservationService;

	@PostMapping("/subscribe")
	public ResponseEntity<String> subscribe(@RequestParam @Valid @Email String email) {

		EmailReservation emailReservation = emailReservationService.saveEmail(email);
		return ResponseEntity.ok("Email saved successfully: " + emailReservation.getEmail());
	}

	@GetMapping
	public ResponseEntity<List<EmailDto>> getAllEmails() {
		return ResponseEntity.ok(emailReservationService.getAllEmails());
	}
}
