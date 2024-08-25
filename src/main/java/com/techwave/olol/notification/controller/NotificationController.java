package com.techwave.olol.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.global.util.SecurityUtil;
import com.techwave.olol.notification.dto.NotificationResDto;
import com.techwave.olol.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Notification", description = "알림 관련 API")
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping
	@Operation(summary = "알림 조회", description = "내가 받은 알림을 조회합니다.(페이징 처리 필요)")
	public ResponseEntity<List<NotificationResDto>> getNotification() {
		return ResponseEntity.ok(notificationService.getNotification(SecurityUtil.getCurrentUserId()));
	}

}

