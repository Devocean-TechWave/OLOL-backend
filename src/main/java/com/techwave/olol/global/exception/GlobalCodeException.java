package com.techwave.olol.global.exception;

import com.techwave.olol.global.dto.ErrorReason;
import com.techwave.olol.global.interfaces.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalCodeException extends RuntimeException {
	private BaseErrorCode errorCode;

	public ErrorReason getErrorReason() {
		return errorCode.getErrorReason();
	}
}
