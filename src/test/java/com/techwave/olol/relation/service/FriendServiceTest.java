package com.techwave.olol.relation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techwave.olol.relation.domain.RelationStatus;
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
	@DisplayName("친구 관계가 없는 경우 빈 리스트를 반환해야 한다.")
	void getFriends_NoFriends() {
		// Arrange
		when(userRelationShipRepository.findAllBySenderIdAndRelationStatusOrReceiverIdAndRelationStatus(
			anyString(), eq(RelationStatus.ACCEPT), anyString(), eq(RelationStatus.ACCEPT)))
			.thenReturn(Collections.emptyList());

		// Act
		List<UserInfoDto> friends = friendService.getFriends("1");

		// Assert
		assertThat(friends).isEmpty();
	}

	@Test
	@DisplayName("친구 관계가 있는 경우 올바른 친구 리스트를 반환해야 한다.")
	void getFriends_WithFriends() {
		// Arrange
		User user1 = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User user2 = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");
		User user3 = createMockUser("3", "Sam Smith", "sams", "http://localhost:8080/profile3.png");

		UserRelationShip relationship1 = createMockRelationship(user1, user2, RelationStatus.ACCEPT);
		UserRelationShip relationship2 = createMockRelationship(user3, user1, RelationStatus.ACCEPT);

		when(userRelationShipRepository.findAllBySenderIdAndRelationStatusOrReceiverIdAndRelationStatus(
			"1", RelationStatus.ACCEPT, "1", RelationStatus.ACCEPT))
			.thenReturn(List.of(relationship1, relationship2));

		// Act
		List<UserInfoDto> friends = friendService.getFriends("1");

		// Assert
		assertThat(friends).hasSize(2);

		// Verify first friend is user2
		assertThat(friends.get(0).getId()).isEqualTo("2");
		assertThat(friends.get(0).getName()).isEqualTo("Jane Doe");
		assertThat(friends.get(0).getNickname()).isEqualTo("janed");
		assertThat(friends.get(0).getProfileUrl()).isEqualTo("http://localhost:8080/profile2.png");

		// Verify second friend is user3
		assertThat(friends.get(1).getId()).isEqualTo("3");
		assertThat(friends.get(1).getName()).isEqualTo("Sam Smith");
		assertThat(friends.get(1).getNickname()).isEqualTo("sams");
		assertThat(friends.get(1).getProfileUrl()).isEqualTo("http://localhost:8080/profile3.png");
	}

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

	@Test
	@DisplayName("친구 요청을 수락할 수 있다.")
	void responseFriend_Accept_Success() {
		// Arrange
		User receiver = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User sender = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.id(100L)
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.relationStatus(RelationStatus.REQUEST) // 요청 상태
			.build();

		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.of(relationship));

		// Act
		friendService.responseFriend("1", 100L, true); // 요청 수락

		// Assert
		assertThat(relationship.getRelationStatus()).isEqualTo(RelationStatus.ACCEPT);
		verify(userRelationShipRepository, times(1)).save(relationship);
	}

	@Test
	@DisplayName("친구 요청을 거절할 수 있다.")
	void responseFriend_Reject_Success() {
		// Arrange
		User receiver = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User sender = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.id(100L)
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.relationStatus(RelationStatus.REQUEST) // 요청 상태
			.build();

		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.of(relationship));

		// Act
		friendService.responseFriend("1", 100L, false); // 요청 거절

		// Assert
		assertThat(relationship.getRelationStatus()).isEqualTo(RelationStatus.REJECT);
		verify(userRelationShipRepository, times(1)).save(relationship);
	}

	@Test
	@DisplayName("다른 사용자의 친구 요청에 응답하려고 할 때 예외를 던진다.")
	void responseFriend_UnauthorizedUser() {
		// Arrange
		User receiver = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User sender = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.id(100L)
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.relationStatus(RelationStatus.REQUEST) // 요청 상태
			.build();

		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.of(relationship));

		// Act & Assert
		assertThatThrownBy(() -> friendService.responseFriend("2", 100L, true))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("다른 사용자의 요청에 대한 응답을 할 수 없습니다.");

		verify(userRelationShipRepository, never()).save(any(UserRelationShip.class));
	}

	@Test
	@DisplayName("이미 수락된 요청에 다시 응답하려고 할 때 예외를 던진다.")
	void responseFriend_AlreadyAccepted() {
		// Arrange
		User receiver = createMockUser("1", "John Doe", "johnd", "http://localhost:8080/profile.png");
		User sender = createMockUser("2", "Jane Doe", "janed", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.id(100L)
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.FRIEND)
			.relationStatus(RelationStatus.ACCEPT) // 이미 수락된 상태
			.build();

		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.of(relationship));

		// Act & Assert
		assertThatThrownBy(() -> friendService.responseFriend("1", 100L, true))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("이미 수락된 요청입니다.");

		verify(userRelationShipRepository, never()).save(any(UserRelationShip.class));
	}

	@Test
	@DisplayName("존재하지 않는 요청에 응답하려고 할 때 예외를 던진다.")
	void responseFriend_RequestNotFound() {
		// Arrange
		when(userRelationShipRepository.findById(100L)).thenReturn(java.util.Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> friendService.responseFriend("1", 100L, true))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 요청입니다.");

		verify(userRelationShipRepository, never()).save(any(UserRelationShip.class));
	}

	@Test
	@DisplayName("친구 삭제가 성공적으로 수행된다.")
	void deleteFriend_Success() {
		// Arrange
		User user = createMockUser("1", "John Doe", "johndoe", "http://localhost:8080/profile.png");
		User friend = createMockUser("2", "Jane Doe", "janedoe", "http://localhost:8080/profile2.png");

		UserRelationShip relationship = UserRelationShip.builder()
			.sender(user)
			.receiver(friend)
			.relationType(RelationType.FRIEND)
			.relationStatus(RelationStatus.ACCEPT)
			.build();

		when(userRelationShipRepository.findBySenderIdAndReceiverId("1", "2"))
			.thenReturn(Optional.of(relationship));

		// Act
		UserInfoDto result = friendService.deleteFriend("1", "2");

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo("2");
		assertThat(result.getName()).isEqualTo("Jane Doe");
		assertThat(result.getNickname()).isEqualTo("janedoe");
		assertThat(result.getProfileUrl()).isEqualTo("http://localhost:8080/profile2.png");

		verify(userRelationShipRepository, times(1)).delete(relationship);
	}

	@Test
	@DisplayName("존재하지 않는 친구를 삭제하려고 할 때 예외를 던진다.")
	void deleteFriend_FriendNotFound() {
		// Arrange
		when(userRelationShipRepository.findBySenderIdAndReceiverId("1", "999"))
			.thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> friendService.deleteFriend("1", "999"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하지 않는 친구입니다.");

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

	private UserRelationShip createMockRelationship(User sender, User receiver, RelationStatus status) {
		return UserRelationShip.builder()
			.sender(sender)
			.receiver(receiver)
			.relationStatus(status)
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
