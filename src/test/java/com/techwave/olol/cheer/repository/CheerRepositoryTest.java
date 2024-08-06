package com.techwave.olol.cheer.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.techwave.olol.cheer.domain.Cheer;
import com.techwave.olol.cheer.domain.CheerType;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CheerRepositoryTest {

	@Autowired
	private CheerRepository cheerRepository;

	@Test
	public void saveTest() {
		Cheer cheer = Cheer.builder()
			.cheerType(CheerType.CHEER)
			.build();
		Cheer savedCheer = cheerRepository.save(cheer);

		assertEquals(1, cheerRepository.count());
		assertEquals(CheerType.CHEER, cheerRepository.findAll().get(0).getCheerType());
		assertEquals(CheerType.CHEER, savedCheer.getCheerType());
	}

	@Test
	public void deleteTest() {
		Cheer cheer = Cheer.builder()
			.cheerType(CheerType.CHEER)
			.build();
		Cheer savedCheer = cheerRepository.save(cheer);

		assertEquals(1, cheerRepository.count());

		cheerRepository.delete(savedCheer);

		assertEquals(0, cheerRepository.count());
	}

}

