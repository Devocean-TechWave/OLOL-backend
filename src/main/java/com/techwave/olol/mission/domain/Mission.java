package com.techwave.olol.mission.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "mission")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "giver_id")
	private User giver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@OneToMany(mappedBy = "mission")
	private List<SuccessStamp> successStamps = new ArrayList<>();

	@Builder
	public Mission(LocalDate startAt, LocalDate endAt, String name, String emoji, String reward, int successQuota,
		boolean isSuccess, boolean isImageRequired) {
		this.startAt = startAt;
		this.endAt = endAt;
		this.name = name;
		this.emoji = emoji;
		this.reward = reward;
		this.successQuota = successQuota;
		this.isSuccess = isSuccess;
		this.isImageRequired = isImageRequired;
	}

	public void setGiver(User giver) {
		this.giver = giver;
		giver.getGivenMissions().add(this);
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
		receiver.getReceivedMissions().add(this);
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
}
