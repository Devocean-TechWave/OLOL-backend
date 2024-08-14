package com.techwave.olol.mission.dto.request;

import com.techwave.olol.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqMissionDto {
	private String receiverId;
	private String name;
	private String emoji;
	private LocalDate startAt;
	private LocalDate endAt;
	private int successQuota;
	private boolean isImageRequired;
	private String reward;
}
