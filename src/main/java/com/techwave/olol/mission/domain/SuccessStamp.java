package com.techwave.olol.mission.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "mission_success_stamp")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessStamp {

	@EmbeddedId
	private SuccessStampId id;

	@MapsId("missionId")
	@ManyToOne(fetch = FetchType.LAZY)
	@Setter
	private Mission mission;

	@Column(name = "image_url")
	private String imageUrl;

	@Builder
	public SuccessStamp(Mission mission, String imageUrl, LocalDate successDate) {
		this.id = new SuccessStampId(mission.getId(), successDate);
		this.mission = mission;
		this.imageUrl = imageUrl;
	}

}

