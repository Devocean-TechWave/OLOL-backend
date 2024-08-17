package com.techwave.olol.relation.dto.response;

import java.time.LocalDateTime;

import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.user.dto.UserInfoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FriendReqDto {
	@Schema(description = "친구 요청 ID", example = "1234567890")
	Long id;
	@Schema(description = "친구 요청을 보낸/받은 유저 정보")
	UserInfoDto userInfo;
	@Schema(description = "요청을 보낸 시간", example = "2024-07-27 17:37:31")
	LocalDateTime requestedAt;

	public static FriendReqDto fromEntity(UserRelationShip relationship) {
		return FriendReqDto.builder()
			.id(relationship.getId())
			.userInfo(UserInfoDto.fromEntity(relationship.getSender()))
			.requestedAt(relationship.getCreatedTime()) // createdAt 필드 필요
			.build();
	}
}
