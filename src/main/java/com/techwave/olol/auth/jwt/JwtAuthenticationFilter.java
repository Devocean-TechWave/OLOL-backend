package com.techwave.olol.auth.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.olol.auth.util.RequestUtil;
import com.techwave.olol.global.dto.ErrorResponse;
import com.techwave.olol.global.exception.GlobalErrorCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final List<String> whiteList;
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			if (checkAuthRequired(request)) {
				String token = RequestUtil.getAccessToken(request);
				if (token != null) {
					Authentication authentication = jwtProvider.getAuthentication(token);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("[JwtAuthenticationFilter] Error: {}", e.getMessage());
			setErrorResponse(request, response, GlobalErrorCode.UNAUTHORIZED);
			// 필터 체인을 진행하지 않고 에러를 클라이언트로 보냄
		}
	}

	private void setErrorResponse(
		HttpServletRequest request,
		HttpServletResponse response,
		GlobalErrorCode errorCode
	) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		response.setStatus(errorCode.getErrorReason().getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		response.setCharacterEncoding("UTF-8"); // 한글 처리를 위한 UTF-8 인코딩 설정
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getErrorReason(), request.getServletPath());
		// sendError로 상태 코드와 응답을 전달
		System.out.println("errorResponse: " + objectMapper.writeValueAsString(errorResponse));
		response.sendError(errorCode.getErrorReason().getStatus(), objectMapper.writeValueAsString(errorResponse));
	}

	private boolean checkAuthRequired(HttpServletRequest request) {
		RequestMatcher rm = new NegatedRequestMatcher(new OrRequestMatcher(
			whiteList.stream()
				.map(AntPathRequestMatcher::new)
				.collect(Collectors.toList())));
		return rm.matcher(request).isMatch();
	}
}
