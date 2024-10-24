package com.techwave.olol.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		log.error("[RestAuthenticationEntryPoint] : {}", request);

		throw new AuthException(AuthErrorCode.ACCESS_TOKEN_NOT_EXIST);
	}
}
