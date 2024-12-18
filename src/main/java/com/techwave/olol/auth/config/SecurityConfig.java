package com.techwave.olol.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techwave.olol.auth.jwt.JwtAuthenticationFilter;
import com.techwave.olol.auth.jwt.JwtProvider;
import com.techwave.olol.auth.jwt.TokenAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
	private final JwtProvider jwtProvider;
	private final SecurityProperties securityProperties;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(HttpBasicConfigurer::disable)
			.csrf(CsrfConfigurer::disable)
			.cors(Customizer.withDefaults())
			.formLogin(FormLoginConfigurer::disable)
			.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.addFilterBefore(
				new JwtAuthenticationFilter(securityProperties.getWhitelist(), jwtProvider),
				UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}


