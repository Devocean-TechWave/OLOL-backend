package com.techwave.olol.mission.exception;

import com.techwave.olol.global.exception.GlobalCodeException;

import lombok.Getter;

@Getter
public class MissionException extends GlobalCodeException {
	public MissionException(MissionErrorCode errorCode) {
		super(errorCode);
	}
}
