package com.techwave.olol.notification.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "oneSignalClient", url = "${onesignal.url}")
public interface OneSignalClient {

	@PostMapping("/notifications")
	String sendNotification(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
		@RequestBody Map<String, Object> notification
	);
}