package com.techwave.olol.user.dto;

import java.time.LocalDate;

import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;
import com.techwave.olol.user.domain.GenderType;
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

	//아이디, 응답, 이름,프로필 사진, 닉네임, 생년월일, 성별, 가족명, 역할
	@Schema(description = "유저 생년월일", example = "1990-01-01")
	private LocalDate birth;
	@Schema(description = "유저 성별", example = "남자")
	private GenderType gender;
	@Schema(description = "유저 가족명", example = "홍길동 가족")
	private String familyName;
	@Schema(description = "유저의 가족 역할", example = "아들")
	private String role;

	// User 엔티티에서 UserInfoDto로 변환하는 정적 팩토리 메소드
	public static UserInfoDto fromEntity(User user) {
		if (user == null)
			throw new AuthException(AuthErrorCode.USER_NOT_EXIST);
		if (user.getFamily() == null)
			throw new AuthException(AuthErrorCode.NO_FAMILY);
		return UserInfoDto.builder()
			.id(user.getId())
			.name(user.getName())
			.nickname(user.getNickname())
			.profileUrl(user.getProfileUrl())
			.birth(user.getBirth())
			.gender(user.getGender())
			.familyName(user.getFamily().getName())
			.role(user.getRole())
			.build();
	}
}
