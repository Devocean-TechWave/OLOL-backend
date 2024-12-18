package com.techwave.olol.auth.jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.techwave.olol.auth.config.JwtProperties;
import com.techwave.olol.auth.dto.TokenDto;
import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;
import com.techwave.olol.user.dto.SecurityUser;

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
			throw new AuthException(AuthErrorCode.ACCESS_TOKEN_NOT_EXIST);
		}

		Collection<? extends GrantedAuthority> authorities =
			Collections.singleton(new SimpleGrantedAuthority("USER"));

		// 클레임에서 subject를 가져와 SecurityUser 객체 생성
		SecurityUser principal = new SecurityUser(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
	}

	public Claims validateToken(String token) {
		Claims claims = jwtUtil.validateToken(token, false);
		if (claims == null)
			throw new AuthException(AuthErrorCode.INVALID_TOKEN);

		return claims;
	}

}
