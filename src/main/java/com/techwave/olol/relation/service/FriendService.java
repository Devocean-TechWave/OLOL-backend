package com.techwave.olol.relation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techwave.olol.relation.domain.RelationStatus;
import com.techwave.olol.relation.domain.RelationType;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.relation.dto.response.FriendReqListDto;
import com.techwave.olol.relation.repository.UserRelationShipRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.UserInfoDto;
import com.techwave.olol.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

	private final UserRelationShipRepository userRelationShipRepository;
	private final UserRepository userRepository;

	/**
	 * 친구 목록을 가져옵니다.
	 * @param userId 사용자 ID
	 * @return 친구 목록
	 */
	public List<UserInfoDto> getFriends(String userId) {
		List<UserRelationShip> relationships = userRelationShipRepository
			.findAllBySenderIdAndRelationStatusOrReceiverIdAndRelationStatus(userId, RelationStatus.ACCEPT,
				userId, RelationStatus.ACCEPT);
		return relationships.stream()
			.map(relationship -> {
				User friend = relationship.getSender().getId().equals(userId) ? relationship.getReceiver() :
					relationship.getSender();
				return new UserInfoDto(friend.getId(), friend.getName(), friend.getNickname(), friend.getProfileUrl());
			})
			.collect(Collectors.toList());
	}

	/**
	 * 내가 보낸 친구 요청 목록을 가져온다.
	 * @param userID 받아 올 사용자 ID
	 * @return 친구 요청 목록
	 */
	public FriendReqListDto getFriendRequest(String userID) {
		return getFriendRequestByUserId(userID, true);
	}

	/**
	 * 내가 받은 친구 요청 목록을 가져온다.
	 * @param userID 받아 올 사용자 ID
	 * @return 친구 요청 목록
	 */
	public FriendReqListDto getSentFriendRequest(String userID) {
		System.out.println("userID = " + userID);
		return getFriendRequestByUserId(userID, false);
	}

	/**
	 * 사용자 ID에 따라 친구 요청 목록을 가져온다.
	 * @param userID 받아 올 사용자 ID
	 * @param isReceiver 내가 받은 요청인지 여부
	 * @return 친구 요청 목록
	 */
	private FriendReqListDto getFriendRequestByUserId(String userID, boolean isReceiver) {
		List<UserRelationShip> friendReq;
		FriendReqListDto friendReqListDto = new FriendReqListDto();
		if (isReceiver) {
			friendReq = userRelationShipRepository.findAllByReceiverIdAndRelationStatus(userID, RelationStatus.REQUEST);
			friendReqListDto.setByUserRelationSenderList(friendReq);
		} else {
			friendReq = userRelationShipRepository.findAllBySenderIdAndRelationStatus(userID, RelationStatus.REQUEST);
			friendReqListDto.setByUserRelationReceiverList(friendReq);
		}
		return friendReqListDto;
	}

	/**
	 * 친구 요청을 한다.
	 * 친구 이미 요청이 있는 경우 예외를 발생시킨다.
	 * @param senderId 보낸 사용자 ID (나)
	 * @param receiverId 받는 사용자 ID (상대방)
	 * @param relationType 친구 관계 타입
	 * @return 친구 요청을 받은 사용자 정보
	 */
	@Transactional
	public UserInfoDto requestFriend(String senderId, String receiverId, RelationType relationType) {
		User sender = userRepository.findUserById(senderId);
		User receiver = userRepository.findUserById(receiverId);

		if (userRelationShipRepository
			.existsBySenderAndReceiverAndRelationTypeAndRelationStatus(sender, receiver, relationType,
				RelationStatus.REQUEST))
			throw new IllegalStateException("이미 동일한 친구 요청이 존재합니다.");
		if (userRelationShipRepository.existsBySenderAndReceiverAndRelationTypeAndRelationStatus(receiver, sender,
			relationType, RelationStatus.ACCEPT))
			throw new IllegalStateException("이미 친구입니다.");
		UserRelationShip userRelationShip = UserRelationShip.builder()
			.sender(sender)
			.receiver(receiver)
			.relationType(relationType)
			.build();

		userRelationShipRepository.save(userRelationShip);
		return UserInfoDto.fromEntity(receiver);
	}

	/**
	 * 내가 보낸 친구 요청을 취소하는 메서드
	 * @param userId 사용자 ID
	 * @param requestId 요청 ID
	 */
	@Transactional
	public void cancelRequestFriend(String userId, Long requestId) {
		// 요청이 존재하는지 확인하고 가져옵니다.
		UserRelationShip relationship = userRelationShipRepository.findById(requestId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청입니다."));

		// 해당 요청이 현재 사용자의 요청인지 확인합니다.
		if (!relationship.getSender().getId().equals(userId)) {
			throw new IllegalStateException("다른 사용자의 요청을 취소할 수 없습니다.");
		}

		// 요청이 이미 취소된 경우 취소할 수 없도록 합니다.
		if (relationship.isDelete()) {
			throw new IllegalStateException("이미 취소된 요청입니다.");
		}

		// 요청을 논리적으로 삭제합니다.
		userRelationShipRepository.delete(relationship);
	}

	/**
	 * 친구 요청에 대한 응답을 합니다.
	 * 이미 수락된 요청에 대한 응답은 할 수 없습니다.
	 * @param userId 사용자 ID
	 * @param requestId 요청 ID
	 * @param isAccept 수락 여부
	 */
	@Transactional
	public void responseFriend(String userId, Long requestId, boolean isAccept) {
		// 요청이 존재하는지 확인하고 가져옵니다.
		UserRelationShip relationship = userRelationShipRepository.findById(requestId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청입니다."));

		// 해당 요청이 현재 사용자에게 온 요청인지 확인합니다.
		if (!relationship.getReceiver().getId().equals(userId)) {
			throw new IllegalStateException("다른 사용자의 요청에 대한 응답을 할 수 없습니다.");
		}

		// 요청이 이미 수락된 경우 수락할 수 없도록 합니다.
		if (relationship.getRelationStatus() != RelationStatus.REQUEST) {
			throw new IllegalStateException("이미 수락된 요청입니다.");
		}

		// 요청을 수락하거나 거절합니다.
		relationship.setRelationStatus(isAccept ? RelationStatus.ACCEPT : RelationStatus.REJECT);
		userRelationShipRepository.save(relationship);
		//TODO: 여기서 유저한테 알림을 보내야 합니다.
	}

	/**
	 * 친구를 삭제합니다.
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @return 삭제된 친구 정보
	 */
	@Transactional
	public UserInfoDto deleteFriend(String userId, String friendId) {
		UserRelationShip relationship = userRelationShipRepository
			.findAllBySenderIdAndReceiverIdAndIsDeleteFalseOrReceiverIdAndSenderIdAndIsDeleteFalse(userId, friendId,
				friendId, userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 친구입니다."));

		userRelationShipRepository.delete(relationship);
		User friend = relationship.getSender().getId().equals(userId) ? relationship.getReceiver() :
			relationship.getSender();
		return UserInfoDto.fromEntity(friend);
	}
}
