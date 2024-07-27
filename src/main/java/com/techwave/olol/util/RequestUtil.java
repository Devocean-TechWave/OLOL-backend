package com.techwave.olol.util;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	public static String getAccessToken(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION_HEADER);
		if (!StringUtils.isEmpty(token) && token.startsWith("Bearer ")) {
			return token.substring(7);
		}

		return null;
	}
}
