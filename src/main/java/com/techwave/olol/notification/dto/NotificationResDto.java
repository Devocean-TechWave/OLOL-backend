package com.techwave.olol.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationResDto {
	@Schema(description = "알림 타입", example = "CHEER")
	NotificationType type;

	@Schema(description = "알림을 보낸 사람의 이름", example = "홍길동")
	String senderName;

	@Schema(description = "알림을 보낸 사람의 ID", example = "uuid-1234-1234-1234-1234")
	String senderId;

	@Schema(description = "알림을 보낸 사람의 프로필 이미지 URL", example = "https://www.naver.com/profile.jpg")
	String profileImageUrl;

	@Schema(description = "알림을 보낸 시간", example = "2021-08-01 12:00:00")
	String createdAt;

}
