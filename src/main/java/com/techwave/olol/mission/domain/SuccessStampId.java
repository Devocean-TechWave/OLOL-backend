package com.techwave.olol.mission.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SuccessStampId implements Serializable {

	@Column(name = "success_date", columnDefinition = "DATE")
	private LocalDate successDate;

	@Column(name = "mission_id", columnDefinition = "BINARY(16)")
	private UUID missionId;

	public SuccessStampId(UUID missionId, LocalDate successDate) {
		this.successDate = successDate;
		this.missionId = missionId;
	}
}
