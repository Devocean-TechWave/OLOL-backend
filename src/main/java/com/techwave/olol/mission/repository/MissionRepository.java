package com.techwave.olol.mission.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.mission.domain.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, UUID> {
	// 미션 조회 쿼리
	Optional<Mission> findById(UUID missionId);

}
