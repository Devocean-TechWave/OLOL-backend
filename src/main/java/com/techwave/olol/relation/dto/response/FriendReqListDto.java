package com.techwave.olol.relation.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.techwave.olol.relation.domain.UserRelationShip;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FriendReqListDto {
	@Schema(description = "친구 요청 목록")
	List<FriendReqDto> friendReqList;

	public void setByUserRelationList(List<UserRelationShip> userRelationList) {
		this.friendReqList = userRelationList.stream()
			.map(FriendReqDto::fromEntity)
			.collect(Collectors.toList());
	}
}
