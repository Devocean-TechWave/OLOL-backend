package com.techwave.olol.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.mission.domain.SuccessStamp;
import com.techwave.olol.mission.domain.SuccessStampId;

@Repository
public interface SuccessStampRepository extends JpaRepository<SuccessStamp, SuccessStampId> {
}
