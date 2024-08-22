package com.techwave.olol.user.dto;

import com.techwave.olol.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserInfoDto {
	@Schema(description = "유저 ID", example = "1234567890")
	private String id;
	@Schema(description = "유저 이름", example = "이름")
	private String name;
	@Schema(description = "유저 닉네임", example = "닉네임")
	private String nickname;
	@Schema(description = "유저 프로필 URL", example = "http://localhost:8080/apidefault_profile.PNG")
	private String profileUrl;

	// User 엔티티에서 UserInfoDto로 변환하는 정적 팩토리 메소드
	public static UserInfoDto fromEntity(User user) {
		return UserInfoDto.builder()
			.id(user.getId())
			.name(user.getName())
			.nickname(user.getNickname())
			.profileUrl(user.getProfileUrl())
			.build();
	}
}
