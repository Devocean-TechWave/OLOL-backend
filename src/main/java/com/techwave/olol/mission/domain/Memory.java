package com.techwave.olol.mission.domain;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.family.domain.Family;
import com.techwave.olol.global.jpa.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
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
import lombok.Setter;

@Table(name = "memory")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
public class Memory extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "create_image_url")
	private String createImageUrl;

	@Column(name = "score")
	@ColumnDefault("0")
	private Long score = 0L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "family_id")
	private Family family;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_id")
	@Setter
	private Mission mission;

}

