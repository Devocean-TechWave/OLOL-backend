package com.techwave.olol.user.domain;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.mission.domain.Memory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "family")
@Getter
public class Family extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "score")
	private Long score;

	@Column(name = "is_delete")
	private boolean isDeleted;

	@OneToMany(mappedBy = "family")
	private List<User> users;

	@OneToMany(mappedBy = "family")
	private List<Memory> familyMissions;
}
