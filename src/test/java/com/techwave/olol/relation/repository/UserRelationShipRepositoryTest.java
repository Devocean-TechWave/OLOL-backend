package com.techwave.olol.relation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.relation.domain.RelationStatus;
import com.techwave.olol.relation.domain.RelationType;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.user.domain.GenderType;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.request.KakaoJoinRequestDto;
import com.techwave.olol.user.repository.UserRepository;

@DataJpaTest
class UserRelationShipRepositoryTest {
	@Autowired
	private UserRelationShipRepository userRelationShipRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("유저간의 관계를 저장할 수 있다.")
	void save() {
		// given
		User sender = createUser("sender", "1234");
		User receiver = createUser("receiver", "5678");

		UserRelationShip userRelationShip = UserRelationShip.builder()
			.sender(sender)
			.receiver(receiver)
			.relationType(RelationType.CAT)
			.build();

		// when
		userRelationShipRepository.save(userRelationShip);

		// then
		UserRelationShip savedUserRelationShip = userRelationShipRepository.findById(userRelationShip.getId()).get();
		Assertions.assertEquals(savedUserRelationShip.getId(), userRelationShip.getId());
	}

	@Test
	@DisplayName("유저가 보낸 친구 요청을 조회할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllBySenderIdAndRelationStatus() {
		// given
		User sender = userRepository.findById("1").orElseThrow();

		// when
		List<UserRelationShip> sentRequests = userRelationShipRepository.findAllBySenderIdAndRelationStatus(
			sender.getId(), RelationStatus.REQUEST);

		// then
		Assertions.assertEquals(9, sentRequests.size());
	}

	@Test
	@DisplayName("유저가 받은 친구 요청이 없을 때 오류가 나지 않는다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllByReceiverIdAndRelationStatus_Empty() {
		// given
		User receiver = userRepository.findById("11").orElseThrow();

		// when
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findAllByReceiverIdAndRelationStatus(
			receiver.getId(), RelationStatus.REQUEST);

		// then
		Assertions.assertEquals(0, receivedRequests.size());
	}

	@Test
	@DisplayName("유저가 받은 친구 요청을 조회할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllByReceiverIdAndRelationStatus() {
		// given
		User receiver = userRepository.findById("1").orElseThrow();

		// when
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findAllByReceiverIdAndRelationStatus(
			receiver.getId(), RelationStatus.REQUEST);

		// then
		Assertions.assertEquals(2, receivedRequests.size());
	}

	@Test
	@DisplayName("유저간의 요청이 이미 있을 때 true를 반환한다.")
	@Sql(scripts = {"/init-relation.sql"})
	void existsBySenderAndReceiverAndRelationType() {
		// given
		User sender = userRepository.findById("1").orElseThrow();
		User receiver = userRepository.findById("2").orElseThrow();

		// when
		boolean exists = userRelationShipRepository.existsBySenderAndReceiverAndRelationTypeAndRelationStatus(sender,
			receiver,
			RelationType.FRIEND, RelationStatus.REQUEST);

		// then
		Assertions.assertTrue(exists);
	}

	@Test
	@DisplayName("유저간의 요청이 없을 때 false를 반환한다.")
	@Sql(scripts = {"/init-relation.sql"})
	void existsBySenderAndReceiverAndRelationType_False() {
		// given
		User sender = userRepository.findById("1").orElseThrow();
		User receiver = userRepository.findById("2").orElseThrow();

		// when
		boolean exists = userRelationShipRepository.existsBySenderAndReceiverAndRelationTypeAndRelationStatus(sender,
			receiver, RelationType.CAT, RelationStatus.REQUEST);

		// then
		Assertions.assertFalse(exists);
	}

	@Test
	@DisplayName("유저 관계를 삭제할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void deleteUserRelationShip() {
		// given
		User sender = userRepository.findById("1").orElseThrow();
		User receiver = userRepository.findById("2").orElseThrow();

		UserRelationShip relationship = userRelationShipRepository.findAllBySenderIdAndRelationStatus(sender.getId(),
				RelationStatus.REQUEST)
			.get(0);

		// when
		userRelationShipRepository.delete(relationship);

		// then
		// 확인 1: 삭제된 엔티티가 더 이상 조회되지 않아야 한다.
		Assertions.assertTrue(userRelationShipRepository.findById(relationship.getId()).isEmpty());

		// 확인 2: 삭제된 엔티티가 existsBySenderAndReceiverAndRelationType에서도 제외되는지 확인
		Assertions.assertFalse(
			userRelationShipRepository.existsBySenderAndReceiverAndRelationTypeAndRelationStatus(sender, receiver,
				RelationType.FRIEND, RelationStatus.REQUEST));
	}

	@Test
	@DisplayName("유저 관계가 삭제된 후 더 이상 조회되지 않는다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllByReceiverIdAndRelationStatus_AfterDelete() {
		// given
		User receiver = userRepository.findById("1").orElseThrow();
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findAllByReceiverIdAndRelationStatus(
			receiver.getId(), RelationStatus.REQUEST);

		UserRelationShip relationshipToDelete = receivedRequests.get(0);

		// 관계 삭제
		userRelationShipRepository.delete(relationshipToDelete);

		// when
		List<UserRelationShip> updatedReceivedRequests = userRelationShipRepository
			.findAllByReceiverIdAndRelationStatus(
				receiver.getId(), RelationStatus.REQUEST);

		// then
		Assertions.assertEquals(receivedRequests.size() - 1, updatedReceivedRequests.size());
		Assertions.assertFalse(updatedReceivedRequests.contains(relationshipToDelete));
	}

	@Test
	@DisplayName("특정 유저가 보낸 요청과 받은 요청 중에서 ACCEPT 상태인 관계를 조회할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllBySenderIdAndRelationStatusAndRelationStatusOrReceiverIdAndRelationStatus_Accepted() {
		// given
		// 스크립트에서 relationStatus가 'REQUEST'로 되어있으므로 테스트를 위해 일부 데이터를 'ACCEPT'로 변경합니다.
		UserRelationShip relationship1 = userRelationShipRepository.findById(1L).orElseThrow();
		relationship1.setRelationStatus(RelationStatus.ACCEPT);
		userRelationShipRepository.save(relationship1);

		UserRelationShip relationship2 = userRelationShipRepository.findById(10L).orElseThrow();
		relationship2.setRelationStatus(RelationStatus.ACCEPT);
		userRelationShipRepository.save(relationship2);

		String userId = "1"; // User1에 대한 테스트

		// when
		List<UserRelationShip> relationships = userRelationShipRepository
			.findAllBySenderIdAndRelationStatusOrReceiverIdAndRelationStatus(
				userId, RelationStatus.ACCEPT, userId, RelationStatus.ACCEPT);

		// then
		Assertions.assertEquals(2, relationships.size()); // ACCEPT 상태인 2개의 관계가 조회되어야 합니다.
		Assertions.assertTrue(relationships.stream()
			.allMatch(rel -> rel.getRelationStatus() == RelationStatus.ACCEPT)); // 모든 관계가 ACCEPT 상태여야 합니다.
	}

	@Test
	@DisplayName("ACCEPT 상태가 아닌 경우는 조회되지 않는다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllBySenderIdAndRelationStatusAndRelationStatusOrReceiverIdAndRelationStatus_NotAccepted() {
		// given
		String userId = "1"; // User1에 대한 테스트

		// when
		List<UserRelationShip> relationships = userRelationShipRepository
			.findAllBySenderIdAndRelationStatusOrReceiverIdAndRelationStatus(
				userId, RelationStatus.ACCEPT, userId, RelationStatus.ACCEPT);

		// then
		Assertions.assertEquals(0, relationships.size()); // 아무런 관계도 ACCEPT 상태가 아니므로 0이어야 합니다.
	}

	@Test
	@DisplayName("발신자와 수신자 간의 관계를 조회할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findBySenderIdAndReceiverId_Found() {
		// given
		String senderId = "1";
		String receiverId = "2";

		// when
		Optional<UserRelationShip> relationship = userRelationShipRepository
			.findAllBySenderIdAndReceiverIdAndIsDeleteFalseOrReceiverIdAndSenderIdAndIsDeleteFalse(
				senderId,
				receiverId, receiverId, senderId);

		// then
		Assertions.assertTrue(relationship.isPresent());
		Assertions.assertEquals(senderId, relationship.get().getSender().getId());
		Assertions.assertEquals(receiverId, relationship.get().getReceiver().getId());
	}

	@Test
	@DisplayName("발신자와 수신자 간의 관계가 없을 때 빈 Optional을 반환한다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findBySenderIdAndReceiverId_NotFound() {
		// given
		String senderId = "1";
		String receiverId = "999"; // 존재하지 않는 사용자 ID

		// when
		Optional<UserRelationShip> relationship = userRelationShipRepository
			.findAllBySenderIdAndReceiverIdAndIsDeleteFalseOrReceiverIdAndSenderIdAndIsDeleteFalse(
				senderId,
				receiverId, receiverId, senderId);

		// then
		Assertions.assertFalse(relationship.isPresent());
	}

	@Test
	@DisplayName("받은 친구 요청을 최신순으로 10개 조회할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findTop10ByReceiverIdAndRelationStatus() {
		// given
		String receiverId = "1";

		// when
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findTop10ByReceiverIdAndRelationStatus(
			receiverId, RelationStatus.REQUEST);

		// then
		Assertions.assertEquals(2, receivedRequests.size());
		Assertions.assertEquals("2", receivedRequests.get(0).getSender().getId());
		Assertions.assertEquals("3", receivedRequests.get(1).getSender().getId());
	}

	@Test
	@DisplayName("보낸 친구 요청을 최신순으로 10개 조회할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findTop10BySenderIdAndRelationStatus() {
		// given
		String senderId = "1";

		// when
		List<UserRelationShip> sentRequests = userRelationShipRepository.findTop10BySenderIdAndRelationStatus(
			senderId, RelationStatus.REQUEST);

		// then
		Assertions.assertEquals(9, sentRequests.size());
		Assertions.assertEquals("2", sentRequests.get(0).getReceiver().getId());
		Assertions.assertEquals("3", sentRequests.get(1).getReceiver().getId());
	}

	@Test
	@DisplayName("받은 친구 요청이 없을 때 빈 리스트를 반환한다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findTop10ByReceiverIdAndRelationStatus_Empty() {
		// given
		String receiverId = "11"; // 존재하지 않는 사용자 ID

		// when
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findTop10ByReceiverIdAndRelationStatus(
			receiverId, RelationStatus.REQUEST);

		// then
		Assertions.assertTrue(receivedRequests.isEmpty());
	}

	@Test
	@DisplayName("보낸 친구 요청이 없을 때 빈 리스트를 반환한다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findTop10BySenderIdAndRelationStatus_Empty() {
		// given
		String senderId = "11"; // 존재하지 않는 사용자 ID

		// when
		List<UserRelationShip> sentRequests = userRelationShipRepository.findTop10BySenderIdAndRelationStatus(
			senderId, RelationStatus.REQUEST);

		// then
		Assertions.assertTrue(sentRequests.isEmpty());
	}

	private User createUser(String nickname, String snsId) {
		User user = User.builder()
			.authType(AuthType.KAKAO)
			.snsId(snsId)
			.build();
		KakaoJoinRequestDto kakaoJoinRequestDto = new KakaoJoinRequestDto();
		kakaoJoinRequestDto.setNickname(nickname);
		kakaoJoinRequestDto.setBirth(LocalDate.of(1994, 1, 1));
		kakaoJoinRequestDto.setGender(GenderType.MALE);
		user.setKakaoUser(kakaoJoinRequestDto);
		return userRepository.save(user);
	}

}
