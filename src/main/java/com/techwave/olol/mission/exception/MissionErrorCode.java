package com.techwave.olol.mission.exception;

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
public enum MissionErrorCode implements BaseErrorCode {
	@ExplainError("미션을 찾을 수 없을 때 발생하는 오류입니다.")
	MISSION_NOT_FOUND(NOT_FOUND, "MISSION_404_1", "미션을 찾을 수 없습니다.");

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
