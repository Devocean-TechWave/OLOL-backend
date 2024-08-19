package com.techwave.olol.mission.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.domain.SuccessStamp;
import com.techwave.olol.mission.dto.request.ReqMissionDto;
import com.techwave.olol.mission.repository.MissionRepository;
import com.techwave.olol.mission.repository.SuccessStampRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class MissionServiceTest {

	@Mock
	private MissionRepository missionRepository;

	@Mock
	private SuccessStampRepository successStampRepository;

	@Mock
	private AmazonS3 amazonS3Client;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private MissionService missionService;

	private User giver;
	private User receiver;
	private User user;
	private ReqMissionDto reqMissionDto;
	private Mission mission;
	private MultipartFile multipartFile;

	@Value("${cloud.aws.bucket.name}")
	private String bucketName;

	@BeforeEach
	void setUp() {
		giver = new User();
		giver.setNickname("giverNickname");

		user = new User();
		user.setNickname("userNickname");

		receiver = new User();
		receiver.setNickname("receiverNickname");

		reqMissionDto = new ReqMissionDto();
		reqMissionDto.setReceiverId("receiverNickname");
		reqMissionDto.setStartAt(LocalDate.now());
		reqMissionDto.setEndAt(LocalDate.now().plusDays(1));
		reqMissionDto.setSuccessQuota(5);

		mission = Mission.createMission(reqMissionDto);
		mission.setGiver(giver);
		mission.setReceiver(receiver);
		mission.setId(UUID.randomUUID());

		multipartFile = new MockMultipartFile("file", "test.png", "image/png", "test image content".getBytes());
	}

	@Test
	@DisplayName("미션 등록이 성공적으로 수행된다.")
	void testRegisterMission() {
		// given
		when(userRepository.findById("giverNickname")).thenReturn(Optional.of(giver));
		when(userRepository.findById("receiverNickname")).thenReturn(Optional.of(receiver));
		when(missionRepository.save(any(Mission.class))).thenReturn(mission);

		// when
		missionService.registerMission("giverNickname", reqMissionDto);

		// then
		verify(missionRepository, times(1)).save(any(Mission.class));
	}

	@Test
	@DisplayName("진행 중인 미션을 성공적으로 조회한다.")
	void testGetProgressMissions() {
		// given
		List<Mission> missions = new ArrayList<>();
		missions.add(mission);

		when(userRepository.findById("giverNickname")).thenReturn(Optional.of(giver));
		when(missionRepository.findProgressMissionsByGiver(giver)).thenReturn(missions);

		// when
		List<Mission> result = missionService.getMissions("giverNickname", true, true);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(mission);
	}

	@Test
	@DisplayName("완료된 미션을 성공적으로 조회한다.")
	void testGetCompletedMissions() {
		// given
		List<Mission> missions = new ArrayList<>();
		missions.add(mission);

		when(userRepository.findById("giverNickname")).thenReturn(Optional.of(giver));
		when(missionRepository.findCompletedMissionsByGiver(giver)).thenReturn(missions);

		// when
		List<Mission> result = missionService.getMissions("giverNickname", false, true);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(mission);
	}

	@Test
	@DisplayName("미션 주차가 현재 주차보다 이전일 경우 예외가 발생한다.")
	void testCheckMissionWeek() throws Exception {
		// given
		LocalDate startDate = LocalDate.now().minusWeeks(2);
		LocalDate endDate = LocalDate.now().plusDays(1);

		// Access the private method using reflection
		Method method = MissionService.class.getDeclaredMethod("checkMissionWeek", LocalDate.class, LocalDate.class);
		method.setAccessible(true);

		// when, then
		try {
			method.invoke(missionService, startDate, endDate);
			fail("Expected IllegalArgumentException to be thrown");
		} catch (Exception e) {
			assertInstanceOf(IllegalArgumentException.class, e.getCause());
			assertEquals("미션 주차는 현재 주차보다 이전일 수 없습니다.", e.getCause().getMessage());
		}
	}

	@Test
	@DisplayName("이미지 저장을 포함한 미션 검증이 성공적으로 수행된다.")
	void testVerifyMissionWithImageSave() throws IOException {
		// given
		when(userRepository.findById("userNickname")).thenReturn(Optional.of(user));
		when(missionRepository.findById(mission.getId())).thenReturn(Optional.of(mission));

		// mock AmazonS3 putObject method
		when(amazonS3Client.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

		// mock AmazonS3 getUrl method to return a dummy URL
		when(amazonS3Client.getUrl(eq(bucketName), anyString())).thenReturn(new URL("http://localhost:8080/test-url"));

		// when
		missionService.verifyMission("userNickname", mission.getId(), multipartFile);

		// then
		verify(userRepository, times(1)).findById("userNickname");
		verify(missionRepository, times(1)).findById(mission.getId());
		verify(amazonS3Client, times(1)).putObject(any(PutObjectRequest.class));
		verify(amazonS3Client, times(1)).getUrl(eq(bucketName), anyString());
		verify(successStampRepository, times(1)).save(any(SuccessStamp.class));  // SuccessStamp가 저장되었는지 확인
		verify(missionRepository, times(1)).save(mission);
	}

	@Test
	@DisplayName("미션 삭제가 성공적으로 수행된다.")
	void testDeleteMission() {
		// given
		UUID missionId = UUID.randomUUID();
		when(userRepository.findById("userNickname")).thenReturn(Optional.of(user));
		when(missionRepository.findById(missionId)).thenReturn(Optional.of(mission));

		// when
		missionService.deleteMission("userNickname", missionId);

		// then
		verify(userRepository, times(1)).findById("userNickname");
		verify(missionRepository, times(1)).findById(missionId);
		verify(missionRepository, times(1)).save(mission);
	}
}