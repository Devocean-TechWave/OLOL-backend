package com.techwave.olol.cheer.domain;

import lombok.Getter;

@Getter
public enum CheerType {
	LIKE("LIKE"),
	LOVE("LOVE"),
	CHEER("CHEER");
	private final String name;

	CheerType(String name) {
		this.name = name;
	}
}
