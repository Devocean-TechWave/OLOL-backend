package com.techwave.olol.relation.domain;

import lombok.Getter;

@Getter
public enum RelationStatus {
	REQUEST("요청중", "REQUEST"),
	ACCEPT("수락", "ACCEPT"),
	REJECT("거절", "REJECT");

	private final String koreanName;
	private final String englishName;

	RelationStatus(String koreanName, String englishName) {
		this.koreanName = koreanName;
		this.englishName = englishName;
	}
}
