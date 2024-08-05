package com.techwave.olol.login.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private Long accessTokenDuration;
	private Long refreshTokenDuration;
}
