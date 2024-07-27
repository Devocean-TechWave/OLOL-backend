package com.techwave.olol.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.techwave.olol.constant.GenderType;
import com.techwave.olol.constant.UserStatus;
import com.techwave.olol.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "유저 정보 response")
@Data
public class UserDto {

	@Schema(example = "54076bf9-be89-4abf-814c-54aa1c225a26")
	private String id;

	@Schema(example = "이름")
	private String name;

	@Schema(example = "닉네임")
	private String nickname;

	@Schema(example = "http://localhost:8080/apidefault_profile.PNG")
	private String profileUrl;

	@Schema(example = "2024-07-27")
	private LocalDate birth;

	@Schema(example = "MALE")
	private GenderType gender;

	@Schema(example = "NORMAL")
	private UserStatus status;

	@Schema(example = "2024-07-27 17:37:31.633324")
	private LocalDateTime createdTime;

	@Schema(example = "2024-07-27 17:37:31.633324")
	private LocalDateTime updatedTime;

	public UserDto(User user) {
		this.id = user.getId();
		this.name = user.getName();

		String nickname = user.getNickname();
		// 탈퇴한 유저 닉네임 처리
		if (user.getStatus() == UserStatus.DELETE)
			nickname = "[탈퇴한 유저]";
		this.nickname = nickname;

		String imageUrl = "";
		if (!StringUtils.isEmpty(user.getProfileUrl())) {
			imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(user.getProfileUrl()).toUriString();
		}

		this.profileUrl = imageUrl;
		this.birth = user.getBirth();
		this.gender = user.getGender();
		this.status = user.getStatus();
		this.createdTime = user.getCreatedTime();
		this.updatedTime = user.getUpdatedTime();
	}
}
