package com.techwave.olol.mission.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.techwave.olol.mission.dto.request.ReqMissionDto;
import com.techwave.olol.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@SQLDelete(sql = "UPDATE Mission SET is_delete = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Mission {
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "start_at", nullable = false, columnDefinition = "DATE")
	private LocalDate startAt;

	@Column(name = "end_at", nullable = false, columnDefinition = "DATE")
	private LocalDate endAt;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "emoji", nullable = false)
	private String emoji;

	@Column(name = "reward")
	private String reward;

	@Column(name = "success_quota", nullable = false)
	private int successQuota;

	@Column(name = "is_success", nullable = false)
	@ColumnDefault("false")
	@Setter
	private boolean isSuccess;

	@Column(name = "is_image_required", nullable = false)
	@ColumnDefault("false")
	private boolean isImageRequired;

	@Column(name = "is_delete", nullable = false)
	@ColumnDefault("false")
	private boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "giver_id")
	private User giver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@Builder.Default
	@OneToMany(mappedBy = "mission")
	private List<SuccessStamp> successStamps = new ArrayList<>();

	public void setGiver(User giver) {
		this.giver = giver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public void addSuccessStamp(SuccessStamp successStamp) {
		this.successStamps.add(successStamp);
	}

	@PrePersist
	@PreUpdate
	private void validateDates() {
		if (this.startAt.isAfter(this.endAt)) {
			throw new IllegalArgumentException("Start date must be before end date");
		}
	}

	// 생성 메서드
	public static Mission createMission(ReqMissionDto request) {
		return Mission.builder()
			.startAt(request.getStartAt())
			.endAt(request.getEndAt())
			.name(request.getName())
			.emoji(request.getEmoji())
			.reward(request.getReward())
			.successQuota(request.getSuccessQuota())
			.isImageRequired(request.isImageRequired())
			.build();
	}
}
