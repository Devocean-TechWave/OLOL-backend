package com.techwave.olol.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techwave.olol.service.ImagesService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/images")
@RestController
public class ImagesController {

	private final ImagesService imagesService;

	@Operation(summary = "이미지 URL")
	@GetMapping("/profile/{id}/{filename}")
	public ResponseEntity<Resource> getProfileImages(
		@PathVariable String id,
		@PathVariable String filename) {
		return imagesService.getProfileImages(id, filename);
	}
}
