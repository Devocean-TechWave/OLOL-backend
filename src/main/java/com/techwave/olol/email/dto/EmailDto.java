package com.techwave.olol.email.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.techwave.olol.email.domain.EmailReservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
	private String email;

	private LocalDateTime createdAt;

	public static List<EmailDto> from(List<EmailReservation> all) {
		return all.stream().map(
				emailReservation -> new EmailDto(emailReservation.getEmail(), emailReservation.getCreatedTime()))
			.collect(Collectors.toList());
	}
}
