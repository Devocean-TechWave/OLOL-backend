package com.techwave.olol.login.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.techwave.olol.global.exception.ApiException;
import com.techwave.olol.global.exception.Error;
import com.techwave.olol.login.config.JwtProperties;
import com.techwave.olol.login.dto.TokenDto;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

	private final JwtUtil jwtUtil;
	private final JwtProperties jwtProperties;

	public TokenDto generateToken(String id) {
		Date now = new Date();
		Date expiration = new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenDuration());

		return jwtUtil.generateToken(id, now, expiration);
	}

	public TokenDto generateRefreshToken() {
		Date now = new Date();
		Date expiration = new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenDuration());

		return jwtUtil.generateToken(null, now, expiration);
	}

	public Authentication getAuthentication(String token) {
		Claims claims = validateToken(token);

		if (StringUtils.isEmpty(claims.getSubject())) {
			throw new ApiException(Error.AUTH_FAILED);
		}

		Collection<? extends GrantedAuthority> authorities =
			Collections.singleton(new SimpleGrantedAuthority("USER"));
		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
	}

	public String getAuthenticationByExpiredToken(String token) {
		Claims claims = validateByExpiredToken(token);

		if (StringUtils.isEmpty(claims.getSubject()))
			throw new ApiException(Error.AUTH_FAILED);

		return claims.getSubject();
	}

	public Claims validateToken(String token) {
		Claims claims = jwtUtil.validateToken(token, false);
		if (claims == null)
			throw new ApiException(Error.TOKEN_VALID_FAILED);

		return claims;
	}

	public Claims validateByExpiredToken(String token) {
		Claims claims = jwtUtil.validateToken(token, true);
		if (claims == null)
			throw new ApiException(Error.TOKEN_VALID_FAILED);

		return claims;
	}
}
