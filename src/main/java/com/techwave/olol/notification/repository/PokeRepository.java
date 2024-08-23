package com.techwave.olol.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.notification.domain.Poke;

@Repository
public interface PokeRepository extends JpaRepository<Poke, Long> {
}
