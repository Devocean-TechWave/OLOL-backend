package com.techwave.olol.mission.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.techwave.olol.mission.domain.Memory;
import com.techwave.olol.mission.domain.Mission;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class MemoryRepositoryTest {
	@Autowired
	private MemoryRepository memoryRepository;

	@Autowired
	private MissionRepository missionRepository;

	@Test
	@DisplayName("미션에 새로운 스템프를 저장할 수 있다.")
	void save() {
		// 실제 코드에서는 미션을 찾아서 그 미션을 넣어주는 방식으로 동작.
		// given
		Mission mission = Mission.builder()
			.startAt(LocalDate.of(2024, 1, 1))
			.endAt(LocalDate.of(2024, 1, 2))
			.build();
		missionRepository.save(mission);

		Memory memory = Memory.builder()
			.mission(missionRepository.findById(mission.getId()).get())
			.successDate(LocalDate.of(2024, 1, 1))
			.build();
		// when
		memoryRepository.save(memory);
		mission.addSuccessStamp(memory);
		// then
		Memory savedMemory = memoryRepository.findById(memory.getId()).get();
		assertThat(savedMemory.getId()).isEqualTo(memory.getId());
	}

}
