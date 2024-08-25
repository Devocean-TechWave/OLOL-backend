package com.techwave.olol.notification.dto;

import lombok.Getter;

@Getter
public enum NotificationType {//내가 받은 응원, 내가 받은 친구 요청, 수락된 친구 요청
	LIKE("LIKE"),
	LOVE("LOVE"),
	CHEER("CHEER"),
	POKE("POKE"),
	FRIEND_REQUEST("FRIEND_REQUEST"),
	ACCEPT_FRIEND_REQUEST("ACCEPT_FRIEND_REQUEST"),
	REJECT_FRIEND_REQUEST("REJECT_FRIEND_REQUEST");
	private final String name;

	NotificationType(String name) {
		this.name = name;
	}
}
