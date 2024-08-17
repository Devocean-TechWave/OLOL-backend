package com.techwave.olol.relation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techwave.olol.relation.domain.RelationType;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.relation.dto.response.FriendReqDto;
import com.techwave.olol.relation.dto.response.FriendReqListDto;
import com.techwave.olol.relation.repository.UserRelationShipRepository;
import com.techwave.olol.user.domain.User;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

	@InjectMocks
	private FriendService friendService;

	@Mock
	private UserRelationShipRepository userRelationShipRepository;

	@Test
	@DisplayName("친구 요청 목록이 비어있을 때 잘 가져올 수 있다.")
	void getFriendRequestTest() {
		// given
		when(userRelationShipRepository.findAllByReceiverId("1")).thenReturn(Collections.emptyList());
		// when
		FriendReqListDto friendRequest = friendService.getFriendRequest("1");
		// then
		assertThat(friendRequest.getFriendReqList()).isEmpty();
	}

	@Test
	@DisplayName("친구 요청 목록이 비어있지 않을 때 잘 가져올 수 있다.")
	void getFriendRequestTest2() {
		// given
		User sender = User.builder()
			.snsId("1")
			.name("John Doe")
			.nickname("johnd")
			.profileUrl("http://localhost:8080/profile.png")
			.build();

		User receiver = User.builder()
			.snsId("2")
			.name("Jane Doe")
			.nickname("janed")
			.profileUrl("http://localhost:8080/profile2.png")
			.build();

		UserRelationShip relationship = UserRelationShip.builder()
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND) // 가정된 enum 값, 실제 사용 중인 값으로 대체 필요
			.build();
		// when
		FriendReqListDto friendRequest = friendService.getFriendRequest("1");
		// then
		assertThat(friendRequest.getFriendReqList()).isNotEmpty();
		assertThat(friendRequest.getFriendReqList()).hasSize(1);
		FriendReqDto dto = friendRequest.getFriendReqList().get(0);
		assertThat(dto.getId()).isEqualTo(relationship.getId());
		assertThat(dto.getUserInfo().getId()).isEqualTo(sender.getId());
		assertThat(dto.getUserInfo().getName()).isEqualTo(sender.getName());
		assertThat(dto.getUserInfo().getNickname()).isEqualTo(sender.getNickname());
		assertThat(dto.getUserInfo().getProfileUrl()).isEqualTo(sender.getProfileUrl());
	}
}