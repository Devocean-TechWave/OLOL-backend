package com.techwave.olol.global.exception;

import com.techwave.olol.global.exception.Error;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiException extends RuntimeException {

	private int code;
	private String message;

	public ApiException(Error error) {
		super(error.getMessage());
		this.code = error.getCode();
		this.message = error.getMessage();
	}

	public ApiException(Error error, Throwable cause) {
		super(error.getMessage(), cause);
		this.code = error.getCode();
		this.message = error.getMessage();
	}
}
