package com.techwave.olol.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.techwave.olol.login.dto.reponse.ApiErrorResponse;
import com.techwave.olol.global.exception.Error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity handleApiException(HttpServletRequest request, ApiException error) {
		log.error("ApiException message({}:{}), path({} {})", error.getCode(), error.getMessage(), request.getMethod(),
			request.getRequestURI());

		return ResponseEntity.ok(new ApiErrorResponse(error));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(HttpServletRequest request, Exception error) {
		log.error("Exception message({}), path({} {})", error.getMessage(), request.getMethod(),
			request.getRequestURI());

		return ResponseEntity.ok(new ApiErrorResponse(error.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity methodArgumentNotValidException(HttpServletRequest request,
		MethodArgumentNotValidException error) {
		String message = error.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(FieldError::getDefaultMessage)
			.findFirst()
			.get();
		log.error("MethodArgumentNotValidException message({}), path({} {})", message, request.getMethod(),
			request.getRequestURI());

		message = Error.INVALID_DATA.getMessage() + " : " + message;
		return ResponseEntity.ok().body(new ApiErrorResponse(message));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity httpMediaTypeNotSupportedException(HttpServletRequest request,
		HttpMediaTypeNotSupportedException error) {
		log.error("HttpMediaTypeNotSupportedException message({}), type({}), path({} {})", error.getMessage(),
			error.getContentType(), request.getMethod(), request.getRequestURI());

		String message = Error.INVALID_DATA.getMessage() + " 타입: " + error.getContentType();
		return ResponseEntity.ok().body(new ApiErrorResponse(message));
	}
}
