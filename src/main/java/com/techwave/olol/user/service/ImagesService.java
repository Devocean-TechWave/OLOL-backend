package com.techwave.olol.user.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.techwave.olol.login.exception.ApiException;
import com.techwave.olol.login.exception.Error;

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
			throw new ApiException(Error.NOT_EXIST_IMAGE, e);
		}

		if (!resource.exists()) {
			throw new ApiException(Error.NOT_EXIST_IMAGE);
		}

		return ResponseEntity.ok()
			.headers(headers)
			.body(resource);
	}
}

