package com.techwave.olol.cheer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.cheer.domain.Cheer;

@Repository
public interface CheerRepository extends JpaRepository<Cheer, Long> {
}
