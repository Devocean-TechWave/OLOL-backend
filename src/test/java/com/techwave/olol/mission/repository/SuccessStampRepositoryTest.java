package com.techwave.olol.mission.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.domain.SuccessStamp;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class SuccessStampRepositoryTest {
	@Autowired
	private SuccessStampRepository successStampRepository;

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

		SuccessStamp successStamp = SuccessStamp.builder()
			.mission(missionRepository.findById(mission.getId()).get())
			.successDate(LocalDate.of(2024, 1, 1))
			.build();
		// when
		successStampRepository.save(successStamp);
		mission.addSuccessStamp(successStamp);
		// then
		SuccessStamp savedSuccessStamp = successStampRepository.findById(successStamp.getId()).get();
		assertThat(savedSuccessStamp.getId()).isEqualTo(successStamp.getId());
	}

}
