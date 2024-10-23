package com.techwave.olol.auth.exception;

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
public enum AuthErrorCode implements BaseErrorCode {
	@ExplainError("accessToken 만료시 발생하는 오류입니다.")
	TOKEN_EXPIRED(UNAUTHORIZED, "AUTH_401_1", "인증 시간이 만료되었습니다. 인증토큰을 재 발급 해주세요"),
	@ExplainError("인증 토큰이 잘못됐을 때 발생하는 오류입니다.")
	INVALID_TOKEN(UNAUTHORIZED, "AUTH_401_2", "잘못된 토큰입니다. 재 로그인 해주세요"),

	@ExplainError("refreshToken 만료시 발생하는 오류입니다.")
	REFRESH_TOKEN_EXPIRED(FORBIDDEN, "AUTH_403_1", "인증 시간이 만료되었습니다. 재 로그인 해주세요."),
	@ExplainError("헤더에 올바른 accessToken을 담지않았을 때 발생하는 오류(형식 불일치 등)")
	ACCESS_TOKEN_NOT_EXIST(FORBIDDEN, "AUTH_403_2", "알맞은 accessToken을 넣어주세요."),
	@ExplainError("유저가 아직 가입이 완료되지 않았을 때 발생하는 오류입니다.")
	USER_NOT_JOIN(FORBIDDEN, "AUTH_403_3", "가입이 완료되지 않았습니다. 가입을 완료해주세요."),
	@ExplainError("유저가 가입 된 가족이 없을 때 발생하는 오류입니다.")
	NO_FAMILY(FORBIDDEN, "AUTH_403_3", "가입된 가족이 없습니다. 가족을 추가해주세요."),

	@ExplainError("인증 타입 불일치시 발생하는 오류입니다.")
	AUTH_TYPE_MISMATCH(BAD_REQUEST, "AUTH_400_1", "인증 타입이 일치하지 않습니다."),
	@ExplainError("이미지가 없는 경우 발생하는 오류입니다.")
	IMAGE_NOT_EXIST(NOT_FOUND, "AUTH_404_1", "이미지가 없습니다."),
	@ExplainError("유저가 없을 때 나타나는 오류입니다.")
	USER_NOT_EXIST(NOT_FOUND, "AUTH_404_2", "유저가 없습니다."),
	@ExplainError("이메일이 중복될 때 나타나는 오류입니다.")
	EMAIL_DUPLICATION(CONFLICT, "AUTH_409_1", "이미 사용중인 이메일입니다.");

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
