package com.techwave.olol.relation.domain;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.notification.domain.Poke;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_relationship")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLRestriction("is_delete = false")
@SQLDelete(sql = "UPDATE user_relationship SET is_delete = true WHERE id = ?")
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

	@Column(name = "relation_status", nullable = false)
	@ColumnDefault("'REQUEST'")
	@Enumerated(EnumType.STRING)
	@Setter
	private RelationStatus relationStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "giver_id")
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@OneToMany(mappedBy = "userRelationShip")
	List<Poke> pokes;

	@Builder
	public UserRelationShip(Long id, User sender, User receiver, RelationType relationType,
		RelationStatus relationStatus,
		boolean isDelete) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.relationType = relationType;
		this.relationStatus = relationStatus;
		this.isDelete = isDelete;
		sender.addSenderRelationShip(this);
		receiver.addReceiverRelationShip(this);
	}
}
