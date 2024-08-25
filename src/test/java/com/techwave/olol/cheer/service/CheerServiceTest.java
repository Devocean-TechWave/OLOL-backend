package com.techwave.olol.cheer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techwave.olol.cheer.domain.Cheer;
import com.techwave.olol.cheer.domain.CheerType;
import com.techwave.olol.cheer.repository.CheerRepository;
import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.repository.MissionRepository;
import com.techwave.olol.notification.domain.Poke;
import com.techwave.olol.notification.repository.PokeRepository;
import com.techwave.olol.notification.service.NotificationService;
import com.techwave.olol.relation.domain.RelationStatus;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.relation.repository.UserRelationShipRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.UserInfoDto;
import com.techwave.olol.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheerService 테스트")
class CheerServiceTest {

	@Mock
	private MissionRepository missionRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CheerRepository cheerRepository;

	@Mock
	private NotificationService notificationService;

	@Mock
	private PokeRepository pokeRepository;

	@Mock
	private UserRelationShipRepository userRelationShipRepository;

	@InjectMocks
	private CheerService cheerService;

	private UUID missionId;
	private String userId;
	private String receiverId;
	private User user;
	private User receiver;
	private Mission mission;
	private CheerType cheerType;
	private UserRelationShip userRelationShip;

	@BeforeEach
	void setUp() {
		missionId = UUID.randomUUID();
		userId = "testUserId";
		receiverId = "testReceiverId";
		user = User.builder()
			.id(userId)
			.build();
		receiver = User.builder()
			.id(receiverId)
			.build();
		mission = Mission.builder().build();
		mission.setReceiver(receiver);
		cheerType = CheerType.LIKE;
		userRelationShip = UserRelationShip.builder()
			.sender(user)
			.receiver(receiver)
			.relationStatus(RelationStatus.ACCEPT)
			.build();
	}

	@Test
	@DisplayName("성공적으로 응원하기")
	void testCheersSuccess() {
		// Given
		when(missionRepository.findById(missionId)).thenReturn(Optional.of(mission));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// When
		UserInfoDto result = cheerService.cheers(userId, missionId, cheerType);

		// Then
		assertNotNull(result);
		assertEquals(receiver.getId(), result.getId());
		verify(cheerRepository, times(1)).save(any(Cheer.class));
		// verify(notificationService, times(1)).sendCheerNotification(any(Cheer.class), eq(receiver)); // Uncomment if notification is enabled
	}

	@Test
	@DisplayName("미션을 찾을 수 없는 경우 예외 발생")
	void testMissionNotFound() {
		// Given
		when(missionRepository.findById(missionId)).thenReturn(Optional.empty());

		// When / Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			cheerService.cheers(userId, missionId, cheerType);
		});

		assertEquals("미션을 찾을 수 없습니다.", exception.getMessage());
		verify(cheerRepository, never()).save(any(Cheer.class));
		// verify(notificationService, never()).sendCheerNotification(any(Cheer.class), any(User.class));
	}

	@Test
	@DisplayName("유저를 찾을 수 없는 경우 예외 발생")
	void testUserNotFound() {
		// Given
		when(missionRepository.findById(missionId)).thenReturn(Optional.of(mission));
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// When / Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			cheerService.cheers(userId, missionId, cheerType);
		});

		assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());
		verify(cheerRepository, never()).save(any(Cheer.class));
		// verify(notificationService, never()).sendCheerNotification(any(Cheer.class), any(User.class));
	}

	@Test
	@DisplayName("성공적으로 콕 찌르기")
	void testRequestSuccess() {
		// Given
		when(userRelationShipRepository.findAcceptedRelationBetweenUsers(userId, receiverId, RelationStatus.ACCEPT))
			.thenReturn(Optional.of(userRelationShip));
		when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

		// When
		UserInfoDto result = cheerService.request(userId, receiverId);

		// Then
		assertNotNull(result);
		assertEquals(receiver.getId(), result.getId());
		verify(pokeRepository, times(1)).save(any(Poke.class));
		// verify(notificationService, times(1)).sendPokeNotification(any(Poke.class)); // Uncomment if notification is enabled
	}

	@Test
	@DisplayName("친구 관계가 아닌 경우 예외 발생")
	void testRequestNotFriend() {
		// Given
		when(userRelationShipRepository.findAcceptedRelationBetweenUsers(userId, receiverId, RelationStatus.ACCEPT))
			.thenReturn(Optional.empty());

		// When / Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			cheerService.request(userId, receiverId);
		});

		assertEquals("친구 관계가 아닙니다.", exception.getMessage());
		verify(pokeRepository, never()).save(any(Poke.class));
		// verify(notificationService, never()).sendPokeNotification(any(Poke.class));
	}

	@Test
	@DisplayName("콕 찔린 유저를 찾을 수 없는 경우 예외 발생")
	void testPokeUserNotFound() {
		// Given
		when(userRelationShipRepository.findAcceptedRelationBetweenUsers(userId, receiverId, RelationStatus.ACCEPT))
			.thenReturn(Optional.of(userRelationShip));
		when(userRepository.findById(receiverId)).thenReturn(Optional.empty());

		// When / Then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			cheerService.request(userId, receiverId);
		});

		assertEquals("콕 찔린 유저를 찾을 수 없습니다.", exception.getMessage());
		verify(pokeRepository, never()).save(any(Poke.class));
		// verify(notificationService, never()).sendPokeNotification(any(Poke.class));
	}
}
