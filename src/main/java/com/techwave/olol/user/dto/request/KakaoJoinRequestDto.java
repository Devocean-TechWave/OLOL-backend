package com.techwave.olol.user.dto.request;

import java.time.LocalDate;

import com.techwave.olol.user.domain.GenderType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KakaoJoinRequestDto {

	@NotNull
	@Schema(description = "이름", example = "홍길동")
	private String name;

	@Size(min = 3, message = "닉네임은 최소 3글자 이상 입력해야 합니다")
	@Schema(description = "닉네임", example = "홍길동")
	private String nickname;

	@NotNull
	@Schema(description = "생년월일", example = "1990-01-01")
	private LocalDate birth;
	@NotNull
	@Schema(description = "성별", example = "M")
	private GenderType gender;
	
}
