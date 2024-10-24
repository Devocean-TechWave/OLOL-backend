package com.techwave.olol.mission.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.mission.domain.Mission;

public interface MissionRepository extends JpaRepository<Mission, UUID> {
	// 미션 조회 쿼리
	Optional<Mission> findById(UUID missionId);

	public void deleteMissionById(UUID missionId);

	public List<Mission> findAllByDate(LocalDate date);
}
