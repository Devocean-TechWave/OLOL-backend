package com.techwave.olol.user.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techwave.olol.global.exception.ApiException;
import com.techwave.olol.global.exception.Error;
import com.techwave.olol.auth.constant.AuthType;
import com.techwave.olol.auth.repository.RefreshTokenRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.NickNameResDto;
import com.techwave.olol.user.dto.UserDto;
import com.techwave.olol.user.dto.UserInfoDto;
import com.techwave.olol.user.dto.request.KakaoJoinRequestDto;
import com.techwave.olol.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	public UserInfoDto getUser(String id) {
		User user = findById(id);
		return UserInfoDto.fromEntity(user);
	}

	public UserDto findByNickname(String nickname) {
		Optional<User> userOpt = userRepository.findByNickname(nickname);
		return userOpt.map(UserDto::new).orElse(null);
	}

	public NickNameResDto checkNickname(String nickname) {
		Optional<User> userOpt = userRepository.findByNickname(nickname);
		return new NickNameResDto(nickname, userOpt.isPresent());
	}

	@Transactional
	public User kakaoJoin(String id, KakaoJoinRequestDto request) {
		User user = findById(id);
		if (user.getAuthType() != AuthType.KAKAO)
			throw new ApiException(Error.AUTH_TYPE_MISMATCH);
		user.setKakaoUser(request);
		return userRepository.save(user);
	}

	//TODO: S3 이미지 업로드로 변경
	@Transactional
	public UserDto updateProfileImage(String id, MultipartFile image) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ApiException(Error.NOT_EXIST_USER));
		try {
			Path path = Paths.get("images/profile/" + id).toAbsolutePath().normalize();
			if (!Files.exists(path))
				Files.createDirectory(path);
			String fileName =
				"profile." + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
			Files.copy(image.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
			String profileUrl = "/images/profile/" + id + "/" + fileName;
			user.setProfileUrl(profileUrl);
			userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("profileUpload: {}, id: {}", e.getMessage(), id);
			throw new ApiException(Error.IMAGE_UPLOAD);
		}
		return new UserDto(user);
	}

	@Transactional
	public void delete(String id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ApiException(Error.NOT_EXIST_USER));
		user.setIsDelete(true);  // 유저 삭제 상태로 설정
		refreshTokenRepository.deleteById(user.getId());
		userRepository.save(user);
	}

	private User findById(String id) {
		return userRepository.findById(id).orElseThrow(() -> new ApiException(Error.NOT_EXIST_USER));
	}
}
