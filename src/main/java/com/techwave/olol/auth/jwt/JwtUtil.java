package com.techwave.olol.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.techwave.olol.auth.dto.TokenDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil implements InitializingBean {

	@Value("${jwt.secret}")
	private String secret;

	private Key secretKey;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenDto generateToken(String id, Date now, Date expiration) {
		JwtBuilder jwtBuilder = Jwts.builder()
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.setIssuer("Olol")
			.setIssuedAt(now)
			.setExpiration(expiration);

		if (id != null)
			jwtBuilder.setSubject(id);

		String token = jwtBuilder.compact();

		return TokenDto.builder()
			.token(token)
			.expiration(expiration)
			.build();
	}

	public Claims validateToken(String token, Boolean allowExpired) {
		try {
			return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		} catch (SecurityException | MalformedJwtException e) {
			log.error("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			if (allowExpired) {
				log.info("만료된 JWT 토큰입니다.");
				return e.getClaims();
			} else {
				log.error("만료된 JWT 토큰입니다.");
			}
		} catch (UnsupportedJwtException e) {
			log.error("지원하지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT 토큰이 잘못되었습니다.");
		}

		return null;
	}
}

