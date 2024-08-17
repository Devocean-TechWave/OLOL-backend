package com.techwave.olol.relation.dto.request;

import com.techwave.olol.relation.domain.RelationType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendRequestDto {
	@Schema(description = "친구 요청을 보낼 사용자 ID", example = "1234567890")
	@NotEmpty(message = "사용자 ID는 필수 항목입니다.")
	String userId; // 사용자 ID
	@Schema(description = "관계 타입", example = "FRIEND")
	@NotNull(message = "관계 타입은 필수 항목입니다.")
	RelationType relationType; // 관계 타입
}
