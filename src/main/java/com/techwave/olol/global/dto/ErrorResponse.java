package com.techwave.olol.global.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private final boolean success = false;
	private final int status;
	private final String code;
	private final String reason;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private final LocalDateTime timeStamp;

	private final String path;

	public ErrorResponse(ErrorReason errorReason, String path) {
		this.status = errorReason.getStatus();
		this.code = errorReason.getCode();
		this.reason = errorReason.getReason();
		this.timeStamp = LocalDateTime.now();
		this.path = path;
	}

	public ErrorResponse(int status, String code, String reason, String path) {
		this.status = status;
		this.code = code;
		this.reason = reason;
		this.timeStamp = LocalDateTime.now();
		this.path = path;
	}
}
