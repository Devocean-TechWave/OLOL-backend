package com.techwave.olol.user.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImagesService {

	private final HttpHeaders headers = new HttpHeaders();

	public ResponseEntity<Resource> getProfileImages(String id, String filename) {
		Resource resource = null;
		try {
			Path path = Paths.get("images/profile/" + id + "/" + filename).toAbsolutePath().normalize();
			resource = new UrlResource(path.toUri());
			if (resource.exists()) {
				String contentType = Files.probeContentType(path);
				headers.add("Content-Type", contentType);
			}
		} catch (Exception e) {
			log.error("get profileImage message: {}", e.getMessage());
			throw new AuthException(AuthErrorCode.IMAGE_NOT_EXIST);
		}

		if (!resource.exists()) {
			throw new AuthException(AuthErrorCode.IMAGE_NOT_EXIST);
		}

		return ResponseEntity.ok()
			.headers(headers)
			.body(resource);
	}
}

