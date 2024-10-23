package com.techwave.olol.global.exception;

import static com.techwave.olol.global.config.OlOlStatic.*;

import java.lang.reflect.Field;
import java.util.Objects;

import com.techwave.olol.global.annotation.ExplainError;
import com.techwave.olol.global.dto.ErrorReason;
import com.techwave.olol.global.interfaces.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
	@ExplainError("백엔드에서 예시로만든 에러입니다. 개발용")
	EXAMPLE_NOT_FOUND(NOT_FOUND, "EXAMPLE_404_1", "예시를 찾을 수 없는 오류입니다."),

	@ExplainError("밸리데이션 (검증 과정 수행속 ) 발생하는 오류입니다.")
	ARGUMENT_NOT_VALID_ERROR(BAD_REQUEST, "GLOBAL_400_1", "검증 오류"),
	@ExplainError("이미지 업로드를 실패했을 때 나는 오류입니다.")
	IMAGE_UPLOAD_ERROR(BAD_REQUEST, "GLOBAL_400_2", "이미지 업로드 오류"),

	@ExplainError("500번대 알수없는 오류입니다. 서버 관리자에게 문의 주세요")
	INTERNAL_SERVER_ERROR(INTERNAL_SERVER, "GLOBAL_500_1", "서버 오류. 관리자에게 문의 부탁드립니다."),

	OTHER_SERVER_BAD_REQUEST(BAD_REQUEST, "FEIGN_400_1", "다른 서버로 한 요청이 Bad Request 입니다."),
	OTHER_SERVER_UNAUTHORIZED(BAD_REQUEST, "FEIGN_400_2", "다른 서버로 한 요청이 Unauthorized 입니다."),
	OTHER_SERVER_FORBIDDEN(BAD_REQUEST, "FEIGN_400_3", "다른 서버로 한 요청이 Forbidden 입니다."),
	OTHER_SERVER_EXPIRED_TOKEN(BAD_REQUEST, "FEIGN_400_4", "다른 서버로 한 요청이 Expired Token 입니다."),
	OTHER_SERVER_NOT_FOUND(BAD_REQUEST, "FEIGN_400_5", "다른 서버로 한 요청이 Not Found 입니다."),
	OTHER_SERVER_INTERNAL_SERVER_ERROR(
		BAD_REQUEST, "FEIGN_400_6", "다른 서버로 한 요청이 Internal Server Error 입니다."),
	SECURITY_CONTEXT_NOT_FOUND(500, "GLOBAL_500_2", "SecurityContext를 찾을 수 없습니다."),

	BAD_FILE_EXTENSION(BAD_REQUEST, "FILE_400_1", "파일 확장자가 잘못 되었습니다."),
	FILE_ONLY_IMAGE(BAD_REQUEST, "FILE_400_2", "이미지 파일만 업로드 가능합니다."),
	DATE_FORMAT_ERROR(BAD_REQUEST, "DATE_400_1", "날짜 형식이 잘못 되었습니다."),
	START_DATE_AFTER_END_DATE(BAD_REQUEST, "DATE_400_2", "시작 날짜가 종료 날짜보다 늦습니다."),
	TOO_MANY_REQUEST(429, "GLOBAL_429_1", "과도한 요청을 보내셨습니다. 잠시 기다려 주세요.");

	private final Integer status;
	private final String code;
	private final String reason;

	@Override
	public ErrorReason getErrorReason() {
		return ErrorReason.builder().reason(reason).code(code).status(status).build();
	}

	@Override
	public String getExplainError() throws NoSuchFieldException {
		Field field = this.getClass().getField(this.name());
		ExplainError annotation = field.getAnnotation(ExplainError.class);
		return Objects.nonNull(annotation) ? annotation.value() : this.getReason();
	}
}
