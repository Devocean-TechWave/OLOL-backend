package com.techwave.olol.relation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.relation.domain.RelationStatus;
import com.techwave.olol.relation.domain.RelationType;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.user.domain.User;

@Repository
public interface UserRelationShipRepository extends JpaRepository<UserRelationShip, Long> {
	List<UserRelationShip> findAllByReceiverId(String receiverId);

	List<UserRelationShip> findAllBySenderId(String senderId);

	boolean existsBySenderAndReceiverAndRelationType(User sender, User receiver, RelationType relationType);

	// 친구 요청을 보냈거나 받은 경우에서 RelationStatus가 ACCEPT인 경우만 가져오는 메서드
	List<UserRelationShip> findAllBySenderIdAndRelationStatusOrReceiverIdAndRelationStatus(
		String senderId, RelationStatus senderStatus, String receiverId, RelationStatus receiverStatus);
}
