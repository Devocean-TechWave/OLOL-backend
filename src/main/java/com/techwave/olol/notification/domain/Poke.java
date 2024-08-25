package com.techwave.olol.notification.domain;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.relation.domain.UserRelationShip;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "poke")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Poke extends BaseEntity {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "date", nullable = false)
	LocalDate date;

	@ManyToOne
	@JoinColumn(name = "user_relation_ship_id")
	private UserRelationShip userRelationShip;
}
