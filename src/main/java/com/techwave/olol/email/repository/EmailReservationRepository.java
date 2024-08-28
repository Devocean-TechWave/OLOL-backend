package com.techwave.olol.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.email.domain.EmailReservation;

@Repository
public interface EmailReservationRepository extends JpaRepository<EmailReservation, Long> {
	Optional<EmailReservation> findByEmail(String email);
}