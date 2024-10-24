package com.techwave.olol.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.mission.domain.Memory;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {

	List<Memory> findAllByFamilyId(Long familyId);
}
