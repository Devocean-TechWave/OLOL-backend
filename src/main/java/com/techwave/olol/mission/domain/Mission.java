package com.techwave.olol.mission.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.mission.dto.request.ReqMissionDto;

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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Table(name = "mission")
@Entity
@Slf4j
@SQLRestriction("is_delete = false")
@SQLDelete(sql = "UPDATE mission SET is_delete = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
public class Mission extends BaseEntity {
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "mission_date", nullable = false, columnDefinition = "DATE")
	private LocalDate date;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "is_delete", nullable = false)
	@ColumnDefault("false")
	private boolean isDeleted = false;

	@Column(name = "point", nullable = false)
	private Long point;

	@Builder.Default
	@OneToMany(mappedBy = "mission")
	private List<Memory> memories = new ArrayList<>();

	public void addMemory(Memory memory) {
		this.memories.add(memory);
	}

	// 생성 메서드
	public static Mission createMission(ReqMissionDto request) {
		return Mission.builder()
			.name(request.getName())
			.description(request.getDescription())
			.point(request.getPoint())
			.build();
	}

	// 수정 메서드
	public void updateByReqDto(ReqMissionDto request) {
		this.name = request.getName();
		this.description = request.getDescription();
		this.point = request.getPoint();
	}

	// 삭제 메서드
	public void deleteMission() {
		this.isDeleted = true;
	}
}
