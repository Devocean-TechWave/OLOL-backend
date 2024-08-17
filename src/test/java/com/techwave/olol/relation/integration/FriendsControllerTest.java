package com.techwave.olol.relation.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.olol.global.WithMockCustomUser;
import com.techwave.olol.relation.dto.response.FriendReqListDto;
import com.techwave.olol.relation.service.FriendService;
import com.techwave.olol.user.dto.UserInfoDto;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "/test-data.sql")
public class FriendsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FriendService friendService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("친구 목록 조회를 할 수 있다.")
	@WithMockCustomUser(id = "user1")
	public void testGetFriends() throws Exception {
		performAndExpectGet("/api/v1/friends", result -> {
			List<UserInfoDto> friends = friendService.getFriends("user1");
			assertThat(friends).isNotEmpty();
			assertThat(friends).anyMatch(friend -> friend.getId().equals("user2"));
		});
	}

	@Test
	@DisplayName("받은 친구 요청 조회 할 수 있다.")
	@WithMockCustomUser(id = "user1")
	public void testGetFriendRequests() throws Exception {
		performAndExpectGet("/api/v1/friends/request", result -> {
			FriendReqListDto friendRequests = friendService.getFriendRequest("user1");
			assertThat(friendRequests.getFriendReqList()).isNotEmpty();
			assertThat(friendRequests.getFriendReqList().get(0).getUserInfo().getId()).isEqualTo("user3");
		});
	}

	@Test
	@DisplayName("보낸 친구 요청 조회 할 수 있다.")
	@WithMockCustomUser(id = "user1")
	public void testGetSentFriendRequests() throws Exception {
		performAndExpectGet("/api/v1/friends/request/sent", result -> {
			FriendReqListDto sentRequests = friendService.getSentFriendRequest("user1");
			assertThat(sentRequests.getFriendReqList()).isNotEmpty();
			assertThat(sentRequests.getFriendReqList().get(0).getUserInfo().getId()).isEqualTo("user2");
		});
	}

	// @Test
	// @DisplayName("친구 요청을 할 수 있다.")
	// @WithMockCustomUser(id = "user1")
	// public void testRequestFriend() throws Exception {
	// 	FriendRequestDto requestDto = new FriendRequestDto("user3", RelationType.FRIEND);
	//
	// 	performAndExpectPost("/api/v1/friends/request", requestDto, result -> {
	// 		UserInfoDto userInfoDto = friendService.requestFriend("user1", "user3", RelationType.FRIEND);
	// 		assertThat(userInfoDto.getId()).isEqualTo("user3");
	// 	});
	// }

	@Test
	@DisplayName("친구 요청을 취소할 수 있다.")
	@WithMockCustomUser(id = "user1")
	public void testCancelRequestFriend() throws Exception {
		performAndExpectDelete("/api/v1/friends/request/1", result -> {
			assertThat(friendService.getSentFriendRequest("user1").getFriendReqList())
				.noneMatch(req -> req.getId().equals(1L));
		});
	}

	@Test
	@DisplayName("친구 요청에 응답할 수 있다.")
	@WithMockCustomUser(id = "user1")
	public void testResponseFriend() throws Exception {
		performAndExpectPost("/api/v1/friends/response/3", true, result -> {
			FriendReqListDto friendRequests = friendService.getFriendRequest("user1");
			assertThat(friendRequests.getFriendReqList())
				.anyMatch(req -> req.getUserInfo().getId().equals("user3"));
		});
	}

	// @Test
	// @DisplayName("친구를 삭제할 수 있다.")
	// @WithMockCustomUser(id = "user1")
	// public void testDeleteFriend() throws Exception {
	// 	performAndExpectDelete("/api/v1/friends/user2", result -> {
	// 		assertThat(friendService.getFriends("user1"))
	// 			.noneMatch(friend -> friend.getId().equals("user2"));
	// 	});
	// }

	private void performAndExpectGet(String url, Consumer<ResultActions> expectations) throws Exception {
		ResultActions resultActions = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		expectations.accept(resultActions);
	}

	private void performAndExpectPost(String url, Object content, Consumer<ResultActions> expectations) throws
		Exception {
		ResultActions resultActions = mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(content)))
			.andExpect(status().isOk());
		expectations.accept(resultActions);
	}

	private void performAndExpectDelete(String url, Consumer<ResultActions> expectations) throws Exception {
		ResultActions resultActions = mockMvc.perform(delete(url)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		expectations.accept(resultActions);
	}
}
