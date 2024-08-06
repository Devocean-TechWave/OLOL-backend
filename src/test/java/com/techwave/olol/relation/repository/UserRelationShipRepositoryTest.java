package com.techwave.olol.relation.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
		User sender = User.builder()
			.authType(AuthType.KAKAO)
			.snsId("1234")
			.build();
		KakaoJoinRequest kakaoJoinRequest = new KakaoJoinRequest();
		kakaoJoinRequest.setNickname("sender");
		kakaoJoinRequest.setBirth(LocalDate.of(1994, 1, 1));
		kakaoJoinRequest.setGender("male");
		sender.setKakaoUser(kakaoJoinRequest);
		userRepository.save(sender);
		User receiver = User.builder()
			.authType(AuthType.KAKAO)
			.snsId("5678")
			.build();
		kakaoJoinRequest = new KakaoJoinRequest();
		kakaoJoinRequest.setNickname("receiver");
		kakaoJoinRequest.setBirth(LocalDate.of(1994, 1, 1));
		kakaoJoinRequest.setGender("male");
		receiver.setKakaoUser(kakaoJoinRequest);
		userRepository.save(receiver);

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
}
