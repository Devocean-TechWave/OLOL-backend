package com.techwave.olol.login.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {
	private String[] whitelist;
}
