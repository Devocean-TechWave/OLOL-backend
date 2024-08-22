package com.techwave.olol.relation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.global.util.SecurityUtil;
import com.techwave.olol.relation.dto.request.FriendRequestDto;
import com.techwave.olol.relation.dto.response.FriendReqListDto;
import com.techwave.olol.relation.service.FriendService;
import com.techwave.olol.user.dto.UserInfoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/friends")
@Tag(name = "Friends", description = "친구 관련 API")
@RequiredArgsConstructor
public class FriendsController {

	private final FriendService friendService;

	@GetMapping
	@Operation(summary = "친구 목록 조회", description = "내 친구 목록을 조회합니다.")
	public ResponseEntity<List<UserInfoDto>> getFriends() {
		return ResponseEntity.ok(friendService.getFriends(SecurityUtil.getCurrentUserId()));
	}

	@GetMapping("/request")
	@Operation(summary = "받은 친구 요청 조회", description = "내가 받은 친구 요청을 조회합니다.")
	public ResponseEntity<FriendReqListDto> getFriendRequest() {
		return ResponseEntity.ok(friendService.getFriendRequest(SecurityUtil.getCurrentUserId()));
	}

	@GetMapping("/request/sent")
	@Operation(summary = "보낸 친구 요청 조회", description = "내가 보낸 친구 요청을 조회합니다.")
	public ResponseEntity<FriendReqListDto> getSentFriendRequest() {
		return ResponseEntity.ok(friendService.getSentFriendRequest(SecurityUtil.getCurrentUserId()));
	}

	@PostMapping("/request")
	@Operation(summary = "친구 요청", description = "친구 요청을 합니다.")
	public ResponseEntity<UserInfoDto> requestFriend(
		@Valid @RequestBody FriendRequestDto friendRequestDto) {
		return ResponseEntity.ok(friendService.requestFriend(SecurityUtil.getCurrentUserId(),
			friendRequestDto.getUserId(), friendRequestDto.getRelationType()));
	}

	@DeleteMapping("/request/{requestId}")
	@Operation(summary = "친구 요청 취소", description = "친구 요청을 취소합니다.")
	public ResponseEntity<String> cancelRequestFriend(@PathVariable Long requestId) {
		friendService.cancelRequestFriend(SecurityUtil.getCurrentUserId(), requestId);
		return ResponseEntity.ok("");
	}

	@PostMapping("response/{requestId}")
	@Operation(summary = "친구 요청 응답", description = "친구 요청을 수락하거나 거절합니다.")
	public ResponseEntity<String> responseFriend(@PathVariable Long requestId,
		@RequestBody @NotNull @NotEmpty boolean isAccept) {
		friendService.responseFriend(SecurityUtil.getCurrentUserId(), requestId, isAccept);
		return ResponseEntity.ok("");
	}

	@DeleteMapping("/{userId}")
	@Operation(summary = "친구 삭제", description = "친구를 삭제합니다.")
	public ResponseEntity<UserInfoDto> deleteFriend(@PathVariable String userId) {
		return ResponseEntity.ok(friendService.deleteFriend(SecurityUtil.getCurrentUserId(), userId));
	}
}

