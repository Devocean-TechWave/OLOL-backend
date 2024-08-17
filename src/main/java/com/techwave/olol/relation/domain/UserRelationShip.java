package com.techwave.olol.relation.domain;

import org.hibernate.annotations.ColumnDefault;

import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_relationship")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRelationShip extends BaseEntity {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "relation_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private RelationType relationType;

	@Column(name = "is_delete", nullable = false)
	@ColumnDefault("false")
	private boolean isDelete;

	@Column(name = "is_accept", nullable = false)
	@ColumnDefault("false")
	private boolean isAccept;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "giver_id")
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@Builder
	public UserRelationShip(User sender, User receiver, RelationType relationType) {
		this.sender = sender;
		this.receiver = receiver;
		this.relationType = relationType;
		sender.addSenderRelationShip(this);
		receiver.addReceiverRelationShip(this);
	}
}
