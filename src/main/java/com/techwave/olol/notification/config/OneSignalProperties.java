package com.techwave.olol.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "onesignal")
public class OneSignalProperties {
	private String url;
	private String appId;
	private String restApiKey;
}
