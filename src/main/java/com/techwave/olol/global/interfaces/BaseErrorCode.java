package com.techwave.olol.global.interfaces;

import com.techwave.olol.global.dto.ErrorReason;

public interface BaseErrorCode {
	public ErrorReason getErrorReason();

	String getExplainError() throws NoSuchFieldException;
}
