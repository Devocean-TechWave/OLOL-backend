package com.techwave.olol.relation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techwave.olol.relation.domain.UserRelationShip;

@Repository
public interface UserRelationShipRepository extends JpaRepository<UserRelationShip, Long> {
}
