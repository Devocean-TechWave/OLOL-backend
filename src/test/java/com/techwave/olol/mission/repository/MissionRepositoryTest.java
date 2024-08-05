package com.techwave.olol.mission.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.techwave.olol.mission.domain.Mission;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class MissionRepositoryTest {

	@Autowired
	private MissionRepository missionRepository;

	@Test
	@DisplayName("미션 생성 및 Default 값이 잘 들어갔다.")
	void createMission() {
		// given
		Mission mission = Mission.builder()
			.startAt(LocalDate.of(2021, 1, 1))
			.endAt(LocalDate.of(2021, 1, 2))
			.name("name")
			.emoji("👍")
			.successQuota(10)
			.build();

		// when
		Mission savedMission = missionRepository.save(mission);

		// then
		assertThat(savedMission.getId()).isNotNull();
		assertThat(savedMission.getName()).isEqualTo("name");
		assertThat(savedMission.getEmoji()).isEqualTo("👍");
		assertThat(savedMission.getSuccessQuota()).isEqualTo(10);
		assertThat(savedMission.isSuccess()).isFalse();
		assertThat(savedMission.isImageRequired()).isFalse();
	}

	@Test
	@DisplayName("미션 삭제가 가능하다.")
	void deleteMission() {
		// given
		Mission mission = Mission.builder()
			.startAt(LocalDate.of(2021, 1, 1))
			.endAt(LocalDate.of(2021, 1, 2))
			.name("name")
			.emoji("👍")
			.successQuota(10)
			.build();
		Mission savedMission = missionRepository.save(mission);

		// when
		missionRepository.delete(savedMission);

		// then
		assertThat(missionRepository.findById(savedMission.getId())).isEmpty();
	}

	@Test
	@DisplayName("미션 시작 시간이 미션 종료 시간보다 같거나 빠르면 에러가 발생한다.")
	void startAtIsBeforeEndAt() {
		// given
		Mission mission = Mission.builder()
			.startAt(LocalDate.of(2021, 1, 2))
			.endAt(LocalDate.of(2021, 1, 1))
			.name("name")
			.emoji("👍")
			.successQuota(10)
			.build();

		// when, then
		assertThatThrownBy(() -> missionRepository.save(mission))
			.isInstanceOf(InvalidDataAccessApiUsageException.class)
			.hasMessageContaining("Start date must be before end date");
	}
}
