package com.techwave.olol.exception;

import com.techwave.olol.constant.Error;

import lombok.Data;

@Data
public class ApiException extends RuntimeException {

	private int code;

	private String message;

	public ApiException(Error error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}
}
