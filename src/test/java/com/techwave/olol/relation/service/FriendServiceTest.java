package com.techwave.olol.relation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

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
import com.techwave.olol.user.dto.UserInfoDto;
import com.techwave.olol.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

	@InjectMocks
	private FriendService friendService;

	@Mock
	private UserRelationShipRepository userRelationShipRepository;

	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("내가 보낸 친구 요청 목록이 비어있을 때 잘 가져올 수 있다.")
	void getFriendRequestTest() {
		// Arrange
		mockFriendRequestRepository(Collections.emptyList(), "1", true);

		// Act
		FriendReqListDto friendRequest = friendService.getFriendRequest("1");

		// Assert
		assertThat(friendRequest.getFriendReqList()).isEmpty();
	}

	@Test
	@DisplayName("내가 보낸 친구 요청 목록이 비어있지 않을 때 잘 가져올 수 있다.")
	void getFriendRequestTest2() {
		// Arrange
		UserRelationShip relationship = createMockRelationship("1", "John Doe", "johnd", "2", "Jane Doe", "janed");
		mockFriendRequestRepository(Collections.singletonList(relationship), "1", true);

		// Act
		FriendReqListDto friendRequest = friendService.getFriendRequest("1");

		// Assert
		assertFriendRequestList(friendRequest, Collections.singletonList(relationship));
	}

	@Test
	@DisplayName("내가 받은 친구 요청 목록이 비어있을 때 잘 가져올 수 있다.")
	void getSentFriendRequestTest() {
		// Arrange
		mockFriendRequestRepository(Collections.emptyList(), "1", false);

		// Act
		FriendReqListDto friendRequest = friendService.getSentFriendRequest("1");

		// Assert
		assertThat(friendRequest.getFriendReqList()).isEmpty();
	}

	@Test
	@DisplayName("내가 받은 친구 요청 목록이 비어있지 않을 때 잘 가져올 수 있다.")
	void getSentFriendRequestTest2() {
		// Arrange
		UserRelationShip relationship = createMockRelationship("1", "John Doe", "johnd", "2", "Jane Doe", "janed");
		mockFriendRequestRepository(Collections.singletonList(relationship), "1", false);

		// Act
		FriendReqListDto friendRequest = friendService.getSentFriendRequest("1");

		// Assert
		assertFriendRequestList(friendRequest, Collections.singletonList(relationship));
	}

	@Test
	@DisplayName("친구 요청을 생성할 수 있다.")
	void requestFriendTest() {
		// Arrange
		User sender = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User receiver = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		when(userRepository.findUserById("1")).thenReturn(sender);
		when(userRepository.findUserById("2")).thenReturn(receiver);
		when(userRelationShipRepository.existsBySenderAndReceiverAndRelationType(sender, receiver, RelationType.FRIEND))
			.thenReturn(false);

		UserRelationShip relationship = UserRelationShip.builder()
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.build();

		when(userRelationShipRepository.save(any())).thenReturn(relationship);

		// Act
		UserInfoDto userInfoDto = friendService.requestFriend("1", "2", RelationType.FRIEND);

		// Assert
		assertUserInfoDtoMatchesUser(userInfoDto, receiver);
	}

	@Test
	@DisplayName("이미 친구 요청이 존재할 때 예외를 던진다.")
	void requestFriendTest2() {
		// Arrange
		User sender = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User receiver = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		when(userRepository.findUserById("1")).thenReturn(sender);
		when(userRepository.findUserById("2")).thenReturn(receiver);
		when(userRelationShipRepository.existsBySenderAndReceiverAndRelationType(sender, receiver, RelationType.FRIEND))
			.thenReturn(true);

		// Act & Assert
		assertThatThrownBy(() -> friendService.requestFriend("1", "2", RelationType.FRIEND))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("이미 동일한 친구 요청이 존재합니다.");
	}

	@Test
	@DisplayName("내가 보낸 친구 요청을 취소할 수 있다.")
	void cancelRequestFriend_Success() {
		// Arrange
		User sender = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User receiver = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.id(100L)
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.isDelete(false)
			.build();

		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.of(relationship));

		// Act
		friendService.cancelRequestFriend("1", 100L);

		// Assert
		verify(userRelationShipRepository, times(1)).delete(relationship);
	}

	@Test
	@DisplayName("다른 사용자의 요청을 취소하려고 할 때 예외를 던진다.")
	void cancelRequestFriend_UnauthorizedUser() {
		// Arrange
		User sender = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User receiver = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.id(100L)
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.isDelete(false)
			.build();

		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.of(relationship));

		// Act & Assert
		assertThatThrownBy(() -> friendService.cancelRequestFriend("2", 100L))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("다른 사용자의 요청을 취소할 수 없습니다.");

		// verify that delete was not called
		verify(userRelationShipRepository, never()).delete(any(UserRelationShip.class));
	}

	@Test
	@DisplayName("이미 취소된 요청을 다시 취소하려고 할 때 예외를 던진다.")
	void cancelRequestFriend_AlreadyDeleted() {
		// Arrange
		User sender = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User receiver = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.id(100L)
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.isDelete(true) // 이미 취소된 요청
			.build();

		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.of(relationship));

		// Act & Assert
		assertThatThrownBy(() -> friendService.cancelRequestFriend("1", 100L))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("이미 취소된 요청입니다.");

		// verify that delete was not called
		verify(userRelationShipRepository, never()).delete(any(UserRelationShip.class));
	}

	@Test
	@DisplayName("존재하지 않는 요청을 취소하려고 할 때 예외를 던진다.")
	void cancelRequestFriend_RequestNotFound() {
		// Arrange
		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> friendService.cancelRequestFriend("1", 100L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 요청입니다.");

		// verify that delete was not called
		verify(userRelationShipRepository, never()).delete(any(UserRelationShip.class));
	}

	// 친구 요청을 Mocking하는 메소드
	private void mockFriendRequestRepository(List<UserRelationShip> relationships, String userId, boolean isReceiver) {
		if (isReceiver) {
			when(userRelationShipRepository.findAllByReceiverId(userId)).thenReturn(relationships);
		} else {
			when(userRelationShipRepository.findAllBySenderId(userId)).thenReturn(relationships);
		}
	}

	// User 객체를 생성하는 메소드
	private User createMockUser(String id, String name, String nickname, String profileUrl) {
		return User.builder()
			.id(id)
			.name(name)
			.nickname(nickname)
			.profileUrl(profileUrl)
			.build();
	}

	// UserRelationShip 객체를 생성하는 메소드
	private UserRelationShip createMockRelationship(String senderId, String senderName, String senderNickname,
		String receiverId, String receiverName, String receiverNickname) {

		User sender = createMockUser(senderId, senderName, senderNickname, "http://localhost:8080/profile.png");
		User receiver = createMockUser(receiverId, receiverName, receiverNickname,
			"http://localhost:8080/profile2.png");

		return UserRelationShip.builder()
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.build();
	}

	// FriendReqListDto를 검증하는 메소드
	private void assertFriendRequestList(FriendReqListDto friendRequest, List<UserRelationShip> relationships) {
		if (relationships.isEmpty()) {
			assertThat(friendRequest.getFriendReqList()).isEmpty();
		} else {
			assertThat(friendRequest.getFriendReqList()).hasSize(relationships.size());

			for (int i = 0; i < relationships.size(); i++) {
				FriendReqDto dto = friendRequest.getFriendReqList().get(i);
				UserRelationShip relationship = relationships.get(i);

				assertThat(dto.getId()).isEqualTo(relationship.getId());
				assertThat(dto.getUserInfo().getId()).isEqualTo(relationship.getSender().getId());
				assertThat(dto.getUserInfo().getName()).isEqualTo(relationship.getSender().getName());
				assertThat(dto.getUserInfo().getNickname()).isEqualTo(relationship.getSender().getNickname());
				assertThat(dto.getUserInfo().getProfileUrl()).isEqualTo(relationship.getSender().getProfileUrl());
			}
		}
	}

	// UserInfoDto를 검증하는 메소드
	private void assertUserInfoDtoMatchesUser(UserInfoDto userInfoDto, User user) {
		assertThat(userInfoDto.getId()).isEqualTo(user.getId());
		assertThat(userInfoDto.getName()).isEqualTo(user.getName());
		assertThat(userInfoDto.getNickname()).isEqualTo(user.getNickname());
		assertThat(userInfoDto.getProfileUrl()).isEqualTo(user.getProfileUrl());
	}
}
