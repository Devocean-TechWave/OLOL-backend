package com.techwave.olol.cheer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.olol.cheer.domain.CheerType;
import com.techwave.olol.cheer.dto.CheerRequestDto;
import com.techwave.olol.global.WithMockCustomUser;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // 테스트 전용 데이터베이스 사용
@Sql(scripts = "/set-all-relation.sql") // 테스트 실행 전 데이터베이스 초기화 SQL 스크립트
@DisplayName("응원 통합 테스트")
public class CheerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	@DisplayName("미션에 응원을 보낼 수 있다.")
	@Transactional
	@WithMockCustomUser(id = "1")
	public void testCheers() throws Exception {
		UUID missionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		CheerRequestDto cheerRequestDto = new CheerRequestDto();
		cheerRequestDto.setCheerType(CheerType.LIKE);

		mockMvc.perform(post("/cheers/" + missionId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cheerRequestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.name").isNotEmpty());
	}

	@Test
	@DisplayName("친구가 아닌 유저에게 응원을 요청하면 오류가 발생한다.")
	@Transactional
	@WithMockCustomUser(id = "1")
	public void testRequest() throws Exception {
		String userId = "2";

		mockMvc.perform(post("/cheers/request/" + userId))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("친구에게 응원을 요청할 수 있다.")
	@Transactional
	@WithMockCustomUser(id = "3")
	public void testRequestToFriend() throws Exception {
		String userId = "4";

		mockMvc.perform(post("/cheers/request/" + userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(userId))
			.andExpect(jsonPath("$.name").isNotEmpty());
	}
	
}