package com.techwave.olol.relation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.relation.dto.response.FriendReqListDto;
import com.techwave.olol.relation.repository.UserRelationShipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

	private final UserRelationShipRepository userRelationShipRepository;

	public FriendReqListDto getFriendRequest(String userID) {
		return getFriendRequestByUserId(userID, true);
	}

	public FriendReqListDto getSentFriendRequest(String userID) {
		return getFriendRequestByUserId(userID, false);
	}

	private FriendReqListDto getFriendRequestByUserId(String userID, boolean isReceiver) {
		List<UserRelationShip> friendReq;

		if (isReceiver) {
			friendReq = userRelationShipRepository.findAllByReceiverId(userID);
		} else {
			friendReq = userRelationShipRepository.findAllBySenderId(userID);
		}

		FriendReqListDto friendReqListDto = new FriendReqListDto();
		friendReqListDto.setByUserRelationList(friendReq);
		return friendReqListDto;
	}

	// public UserInfoDto requestFriend(String userId) {
	// 	return new UserInfoDto();
	// }

	public void cancelRequestFriend(String requestId) {
		// TODO Auto-generated method stub
	}

	public void responseFriend(String requestId) {
		// TODO Auto-generated method stub
	}

	// public UserInfoDto deleteFriend(String userId) {
	// 	return new UserInfoDto();
	// }

}
