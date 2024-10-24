package com.techwave.olol.family.dto;

import java.util.ArrayList;
import java.util.List;

import com.techwave.olol.family.domain.Family;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.UserInfoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "가족 정보")
public class FamilyInfoDto {
	@Schema(description = "가족 ID", example = "1L")
	private Long familyId;
	@Schema(description = "가족 이름", example = "홍길동 가족")
	private String familyName;
	@Schema(description = "랭킹", example = "1")
	private Long ranking;
	@Schema(description = "점수", example = "100")
	private Long score;

	@Schema(description = "가족 구성원")
	List<UserInfoDto> familyMembers = new ArrayList<>();

	void setFamilyMembers(List<User> users) {
		for (User user : users) {
			familyMembers.add(UserInfoDto.fromEntity(user));
		}
	}

	public static FamilyInfoDto fromEntity(Family family) {
		FamilyInfoDto familyInfoDto = new FamilyInfoDto();
		familyInfoDto.setFamilyId(family.getId());
		familyInfoDto.setFamilyName(family.getName());
		familyInfoDto.setScore(family.getScore());
		familyInfoDto.setFamilyMembers(family.getUsers());
		return familyInfoDto;
	}

}
