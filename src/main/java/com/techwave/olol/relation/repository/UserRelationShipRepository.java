package com.techwave.olol.relation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.relation.domain.RelationType;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.user.domain.User;

@Repository
public interface UserRelationShipRepository extends JpaRepository<UserRelationShip, Long> {
	List<UserRelationShip> findAllByReceiverId(String receiverId);

	List<UserRelationShip> findAllBySenderId(String senderId);

	boolean existsBySenderAndReceiverAndRelationType(User sender, User receiver, RelationType relationType);
}
