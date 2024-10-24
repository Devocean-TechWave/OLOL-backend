package com.techwave.olol.mission.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.techwave.olol.mission.domain.Mission;

import jakarta.annotation.PostConstruct;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MissionRepositoryTest {

	@TestConfiguration
	static class TestConfig {

		@PostConstruct
		void setUpLogging() {
			System.setProperty("org.jboss.logging.provider", "slf4j");
		}

		@Bean
		public org.slf4j.Logger logger() {
			return org.slf4j.LoggerFactory.getLogger("org.hibernate.SQL");
		}
	}

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
			.build();

		// when
		Mission savedMission = missionRepository.save(mission);

		// then
		assertThat(savedMission.getId()).isNotNull();
		assertThat(savedMission.getName()).isEqualTo("name");
	}

	@Test
	@DisplayName("미션 삭제가 가능하다.")
	void deleteMission() {
		// given
		Mission mission = Mission.builder()
			.startAt(LocalDate.of(2021, 1, 1))
			.endAt(LocalDate.of(2021, 1, 2))
			.name("name")
			.build();
		Mission savedMission = missionRepository.save(mission);

		// when
		missionRepository.delete(savedMission);
		//isDelete 제대로 적용됐는지 로그 보려면 주석 해제
		// missionRepository.findAll().forEach(System.out::println);
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
			.build();

		// when, then
		assertThatThrownBy(() -> missionRepository.save(mission))
			.isInstanceOf(InvalidDataAccessApiUsageException.class)
			.hasMessageContaining("Start date must be before end date");
	}
}
