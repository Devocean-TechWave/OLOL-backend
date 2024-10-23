package com.techwave.olol.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NickNameResDto {
	@Schema(description = "닉네임", example = "홍길동")
	private String nickName;
	@Schema(description = "닉네임 중복 여부", example = "false")
	private boolean exist;
}
