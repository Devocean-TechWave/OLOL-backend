package com.techwave.olol.family.dto;

import java.util.List;

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
	List<UserInfoDto> familyMembers;
}
