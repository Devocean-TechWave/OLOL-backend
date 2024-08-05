package com.techwave.olol.mission.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.mission.domain.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, UUID> {
}
