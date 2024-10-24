package com.techwave.olol.family.exception;

import java.lang.reflect.Field;
import java.util.Objects;

import com.techwave.olol.global.annotation.ExplainError;
import com.techwave.olol.global.dto.ErrorReason;
import com.techwave.olol.global.interfaces.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FamilyErrorCode implements BaseErrorCode {
	@ExplainError("가족이 없을 때 발생하는 오류입니다.")
	NO_FAMILY(404, "FAMILY_404_1", "가족이 없습니다."),
	@ExplainError("가족이 이미 존재할 때 발생하는 오류입니다.")
	FAMILY_ALREADY_EXIST(409, "FAMILY_409_1", "가족이 이미 존재합니다."),
	@ExplainError("가족이 존재하지 않을 때 발생하는 오류입니다.")
	FAMILY_NOT_EXIST(404, "FAMILY_404_2", "가족이 존재하지 않습니다.");
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
