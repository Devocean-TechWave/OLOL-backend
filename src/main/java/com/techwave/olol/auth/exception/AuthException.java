package com.techwave.olol.auth.exception;

import com.techwave.olol.global.exception.GlobalCodeException;

import lombok.Getter;

@Getter
public class AuthException extends GlobalCodeException {
	public AuthException(AuthErrorCode errorCode) {
		super(errorCode);
	}
}
