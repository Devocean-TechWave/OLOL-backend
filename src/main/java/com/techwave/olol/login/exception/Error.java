package com.techwave.olol.login.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {

	SERVER_ERROR(999, "Sever Error"),

	EMPTY_PARAM(1001, "빈 파라미터"),
	IMAGE_UPLOAD(1002, "이미지 업로드 오류"),
	EMPTY_DATA(1003, "빈 데이터"),

	NOT_EXIST_USER(2001, "존재하지 않는 유저입니다."),
	NOT_EXIST_IMAGE(2002, "존재하지 않는 이미지입니다."),

	TOKEN_VALID_FAILED(3001, "토큰 검증 실패"),
	INVALID_DATA(3002, "데이터 오류"),
	AUTH_TYPE_MISMATCH(3003, "인증 타입 불일치"),
	AUTH_FAILED(3004, "인증 실패"),

	UNAUTHORIZED(4001, "인증이 필요합니다."),
	ACCESS_DENIED(4002, "접근 거부된 유저입니다.");

	private int code;

	private String message;
}
