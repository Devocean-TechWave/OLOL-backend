package com.techwave.olol.dto.reponse;

import com.techwave.olol.constant.Error;
import com.techwave.olol.exception.ApiException;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApiErrorResponse {

	private Integer code;

	private String message;

	public ApiErrorResponse(ApiException error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}

	public ApiErrorResponse(Error error) {
		this.code = error.getCode();
		this.message = error.getMessage();
	}

	public ApiErrorResponse(String message) {
		this.code = Error.INVALID_DATA.getCode();
		this.message = message;
	}
}