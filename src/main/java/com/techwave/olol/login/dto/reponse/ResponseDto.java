package com.techwave.olol.login.dto.reponse;

import lombok.Data;

@Data
public class ResponseDto {

	private Object data;

	public ResponseDto(Object data) {
		this.data = data;
	}
}
