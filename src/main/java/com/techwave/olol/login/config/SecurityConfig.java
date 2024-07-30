package com.techwave.olol.login.config;

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

import com.techwave.olol.login.auth.JwtAuthenticationFilter;
import com.techwave.olol.login.auth.JwtProvider;
import com.techwave.olol.login.auth.TokenAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(securityProperties.getWhitelist()).permitAll()
				.anyRequest().authenticated())
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider, securityProperties),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.accessDeniedHandler(tokenAccessDeniedHandler));

		return http.build();
	}
}
