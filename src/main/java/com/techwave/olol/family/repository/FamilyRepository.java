package com.techwave.olol.family.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techwave.olol.family.domain.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {
	Optional<Family> findByName(String name);

	// 가족 점수를 기준으로 내림차순 정렬된 목록을 반환
	List<Family> findAllByOrderByScoreDesc();
}
