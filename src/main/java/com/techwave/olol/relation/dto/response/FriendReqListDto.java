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

	public void setByUserRelationSenderList(List<UserRelationShip> userRelationList) {
		this.friendReqList = userRelationList.stream()
			.map(FriendReqDto::fromSenderEntity)
			.collect(Collectors.toList());
	}

	public void setByUserRelationReceiverList(List<UserRelationShip> userRelationList) {
		this.friendReqList = userRelationList.stream()
			.map(FriendReqDto::fromReceiverEntity)
			.collect(Collectors.toList());
	}
}
