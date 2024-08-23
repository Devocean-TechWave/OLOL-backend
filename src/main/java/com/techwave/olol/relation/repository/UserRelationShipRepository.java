package com.techwave.olol.relation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.relation.domain.RelationStatus;
import com.techwave.olol.relation.domain.RelationType;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.user.domain.User;

@Repository
public interface UserRelationShipRepository extends JpaRepository<UserRelationShip, Long> {
	List<UserRelationShip> findAllByReceiverIdAndRelationStatus(String receiverId, RelationStatus relationStatus);

	List<UserRelationShip> findAllBySenderIdAndRelationStatus(String senderId, RelationStatus relationStatus);

	boolean existsBySenderAndReceiverAndRelationTypeAndRelationStatus(User sender, User receiver,
		RelationType relationType, RelationStatus relationStatus);

	// 친구 요청을 보냈거나 받은 경우에서 RelationStatus가 ACCEPT인 경우만 가져오는 메서드
	List<UserRelationShip> findAllBySenderIdAndRelationStatusOrReceiverIdAndRelationStatus(
		String senderId, RelationStatus senderStatus, String receiverId, RelationStatus receiverStatus);

	Optional<UserRelationShip> findAllBySenderIdAndReceiverIdAndIsDeleteFalseOrReceiverIdAndSenderIdAndIsDeleteFalse(
		String senderId, String receiverId, String receiverId2, String senderId2);

	List<UserRelationShip> findTop10ByReceiverIdAndRelationStatus(String receiverId, RelationStatus relationStatus);

	List<UserRelationShip> findTop10BySenderIdAndRelationStatus(String senderId, RelationStatus relationStatus);
}
