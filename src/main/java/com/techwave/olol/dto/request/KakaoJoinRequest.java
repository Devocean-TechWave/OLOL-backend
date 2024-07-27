package com.techwave.olol.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KakaoJoinRequest {

	private String name;

	@Size(min = 3, message = "닉네임은 최소 3글자 이상 입력해야 합니다")
	private String nickname;

	private LocalDate birth;

	private String gender;
}
