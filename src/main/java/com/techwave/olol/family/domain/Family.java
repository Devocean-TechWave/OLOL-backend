package com.techwave.olol.family.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.mission.domain.Memory;
import com.techwave.olol.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "family")
@Builder
@AllArgsConstructor
@Getter
public class Family extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "score")
	@ColumnDefault("0")
	@Builder.Default
	private Long score = 0L;

	@Column(name = "is_delete")
	private boolean isDeleted = false;

	@OneToMany(mappedBy = "family")
	@Builder.Default
	private List<User> users = new ArrayList<>();

	@OneToMany(mappedBy = "family")
	@Builder.Default
	private List<Memory> familyMissions = new ArrayList<>();

	public void addUser(User user) {
		users.add(user);
		user.setFamily(this);
	}
}
