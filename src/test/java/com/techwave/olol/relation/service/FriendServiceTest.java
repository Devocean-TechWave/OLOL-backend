package com.techwave.olol.relation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
	@DisplayName("내가 보낸 친구 요청 목록이 비어있을 때 잘 가져올 수 있다.")
	void getFriendRequestTest() {
		testFriendRequest(Collections.emptyList(), friendService::getFriendRequest, "1", true);
	}

	@Test
	@DisplayName("내가 보낸 친구 요청 목록이 비어있지 않을 때 잘 가져올 수 있다.")
	void getFriendRequestTest2() {
		UserRelationShip relationship = createMockRelationship("1", "John Doe", "johnd", "2", "Jane Doe", "janed");

		testFriendRequest(Collections.singletonList(relationship), friendService::getFriendRequest, "1", true);
	}

	@Test
	@DisplayName("내가 받은 친구 요청 목록이 비어있을 때 잘 가져올 수 있다.")
	void getSentFriendRequestTest() {
		testFriendRequest(Collections.emptyList(), friendService::getSentFriendRequest, "1", false);
	}

	@Test
	@DisplayName("내가 받은 친구 요청 목록이 비어있지 않을 때 잘 가져올 수 있다.")
	void getSentFriendRequestTest2() {
		UserRelationShip relationship = createMockRelationship("1", "John Doe", "johnd", "2", "Jane Doe", "janed");

		testFriendRequest(Collections.singletonList(relationship), friendService::getSentFriendRequest, "1", false);
	}

	// 친구 요청 목록을 테스트하는 공통 메소드
	private void testFriendRequest(List<UserRelationShip> relationships,
		Function<String, FriendReqListDto> requestFunction,
		String userId, boolean isReceiver) {

		if (isReceiver) {
			when(userRelationShipRepository.findAllByReceiverId(userId)).thenReturn(relationships);
		} else {
			when(userRelationShipRepository.findAllBySenderId(userId)).thenReturn(relationships);
		}

		FriendReqListDto friendRequest = requestFunction.apply(userId);

		if (relationships.isEmpty()) {
			assertThat(friendRequest.getFriendReqList()).isEmpty();
		} else {
			assertThat(friendRequest.getFriendReqList()).isNotEmpty();
			assertThat(friendRequest.getFriendReqList()).hasSize(1);

			FriendReqDto dto = friendRequest.getFriendReqList().get(0);
			UserRelationShip relationship = relationships.get(0);

			assertThat(dto.getId()).isEqualTo(relationship.getId());
			assertThat(dto.getUserInfo().getId()).isEqualTo(relationship.getSender().getId());
			assertThat(dto.getUserInfo().getName()).isEqualTo(relationship.getSender().getName());
			assertThat(dto.getUserInfo().getNickname()).isEqualTo(relationship.getSender().getNickname());
			assertThat(dto.getUserInfo().getProfileUrl()).isEqualTo(relationship.getSender().getProfileUrl());
		}
	}

	// 관계를 설정하는 공통 메소드
	private UserRelationShip createMockRelationship(String senderId, String senderName, String senderNickname,
		String receiverId, String receiverName, String receiverNickname) {

		User sender = User.builder()
			.snsId(senderId)
			.name(senderName)
			.nickname(senderNickname)
			.profileUrl("http://localhost:8080/profile.png")
			.build();

		User receiver = User.builder()
			.snsId(receiverId)
			.name(receiverName)
			.nickname(receiverNickname)
			.profileUrl("http://localhost:8080/profile2.png")
			.build();

		return UserRelationShip.builder()
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND) // 실제 사용 중인 enum 값으로 대체 필요
			.build();
	}
}