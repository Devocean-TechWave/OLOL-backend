package com.techwave.olol.relation.repository;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.relation.domain.RelationType;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.request.KakaoJoinRequest;
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
	void findAllBySenderId() {
		// given
		User sender = userRepository.findById("1").orElseThrow();

		// when
		List<UserRelationShip> sentRequests = userRelationShipRepository.findAllBySenderId(sender.getId());

		// then
		Assertions.assertEquals(9, sentRequests.size());
	}

	@Test
	@DisplayName("유저가 받은 친구 요청이 없을 때 오류가 나지 않는다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllByReceiverId_Empty() {
		// given
		User receiver = userRepository.findById("11").orElseThrow();

		// when
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findAllByReceiverId(receiver.getId());

		// then
		Assertions.assertEquals(0, receivedRequests.size());
	}

	@Test
	@DisplayName("유저가 받은 친구 요청을 조회할 수 있다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllByReceiverId() {
		// given
		User receiver = userRepository.findById("1").orElseThrow();

		// when
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findAllByReceiverId(receiver.getId());

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
		boolean exists = userRelationShipRepository.existsBySenderAndReceiverAndRelationType(sender, receiver,
			RelationType.FRIEND);

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
		boolean exists = userRelationShipRepository.existsBySenderAndReceiverAndRelationType(sender, receiver,
			RelationType.CAT);

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

		UserRelationShip relationship = userRelationShipRepository.findAllBySenderId(sender.getId()).get(0);

		// when
		userRelationShipRepository.delete(relationship);

		// then
		// 확인 1: 삭제된 엔티티가 더 이상 조회되지 않아야 한다.
		Assertions.assertTrue(userRelationShipRepository.findById(relationship.getId()).isEmpty());

		// 확인 2: 삭제된 엔티티가 existsBySenderAndReceiverAndRelationType에서도 제외되는지 확인
		Assertions.assertFalse(
			userRelationShipRepository.existsBySenderAndReceiverAndRelationType(sender, receiver, RelationType.FRIEND));
	}

	@Test
	@DisplayName("유저 관계가 삭제된 후 더 이상 조회되지 않는다.")
	@Sql(scripts = {"/init-relation.sql"})
	void findAllByReceiverId_AfterDelete() {
		// given
		User receiver = userRepository.findById("1").orElseThrow();
		List<UserRelationShip> receivedRequests = userRelationShipRepository.findAllByReceiverId(receiver.getId());

		UserRelationShip relationshipToDelete = receivedRequests.get(0);

		// 관계 삭제
		userRelationShipRepository.delete(relationshipToDelete);

		// when
		List<UserRelationShip> updatedReceivedRequests = userRelationShipRepository.findAllByReceiverId(
			receiver.getId());

		// then
		Assertions.assertEquals(receivedRequests.size() - 1, updatedReceivedRequests.size());
		Assertions.assertFalse(updatedReceivedRequests.contains(relationshipToDelete));
	}

	private User createUser(String nickname, String snsId) {
		User user = User.builder()
			.authType(AuthType.KAKAO)
			.snsId(snsId)
			.build();
		KakaoJoinRequest kakaoJoinRequest = new KakaoJoinRequest();
		kakaoJoinRequest.setNickname(nickname);
		kakaoJoinRequest.setBirth(LocalDate.of(1994, 1, 1));
		kakaoJoinRequest.setGender("male");
		user.setKakaoUser(kakaoJoinRequest);
		return userRepository.save(user);
	}
}