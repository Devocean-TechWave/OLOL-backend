package com.techwave.olol.notification.domain;

import java.time.LocalDate;

import com.techwave.olol.relation.domain.UserRelationShip;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "poke")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Poke {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "date", nullable = false)
	LocalDate date;

	@ManyToOne
	@JoinColumn(name = "user_relation_ship_id")
	private UserRelationShip userRelationShip;
}
