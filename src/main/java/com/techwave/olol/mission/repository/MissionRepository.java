package com.techwave.olol.mission.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.user.domain.User;

@Repository
public interface MissionRepository extends JpaRepository<Mission, UUID> {
	// 미션 조회 쿼리
	@Query("SELECT m FROM Mission m WHERE m.giver = :giver AND m.endAt < CURRENT_DATE AND m.isSuccess = false")
	List<Mission> findCompletedMissionsByGiver(@Param("giver") User giver);

	@Query("SELECT m FROM Mission m WHERE m.giver = :giver AND m.endAt >= CURRENT_DATE AND m.isSuccess = false")
	List<Mission> findProgressMissionsByGiver(@Param("giver") User giver);

	@Query("SELECT m FROM Mission m WHERE m.receiver = :receiver AND m.endAt < CURRENT_DATE AND m.isSuccess = false")
	List<Mission> findCompletedMissionsByReceiver(@Param("receiver") User receiver);

	@Query("SELECT m FROM Mission m WHERE m.receiver = :receiver AND m.endAt >= CURRENT_DATE AND m.isDeleted = false")
	List<Mission> findProgressMissionsByReceiver(@Param("receiver") User receiver);

	List<Mission> findByGiver(User receiver);

	List<Mission> findByReceiver(User receiver);

	Optional<Mission> findById(UUID missionId);
}
