package com.techwave.olol.family.exception;

import com.techwave.olol.global.exception.GlobalCodeException;

import lombok.Getter;

@Getter
public class FamilyException extends GlobalCodeException {
	public FamilyException(FamilyErrorCode errorCode) {
		super(errorCode);
	}
}
