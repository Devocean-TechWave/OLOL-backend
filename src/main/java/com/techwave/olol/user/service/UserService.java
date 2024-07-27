package com.techwave.olol.user.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.login.exception.ApiException;
import com.techwave.olol.login.exception.Error;
import com.techwave.olol.login.repository.RefreshTokenRepository;
import com.techwave.olol.user.dto.UserDto;
import com.techwave.olol.user.dto.request.EditUserRequest;
import com.techwave.olol.user.dto.request.KakaoJoinRequest;
import com.techwave.olol.user.model.User;
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

	public UserDto getUser(String id) {
		User user = findById(id);

		return new UserDto(user);
	}

	public UserDto findByNickname(String nickname) {
		User user = userRepository.findByNickname(nickname);

		return user != null ? new UserDto(user) : null;
	}

	public boolean checkNickname(String nickname) {
		User user = userRepository.findByNickname(nickname);
		return user == null;
	}

	@Transactional
	public void kakaoJoin(String id, KakaoJoinRequest request) {
		User user = findById(id);
		if (user.getAuthType() != AuthType.KAKAO)
			throw new ApiException(Error.AUTH_TYPE_MISMATCH);
		user.setKakaoUser(request);

		userRepository.save(user);
	}

	@Transactional
	public UserDto edit(String id, EditUserRequest data, MultipartFile file) {
		User user = userRepository.findById(id).orElseThrow(() -> new ApiException(Error.NOT_EXIST_USER));
		if (data != null && !StringUtils.isEmpty(data.getNickname()) && checkNickname(data.getNickname())) {
			user.setNickname(data.getNickname());
		}

		try {
			if (file != null && !file.isEmpty()) {
				Path path = Paths.get("images/profile/" + user.getId()).toAbsolutePath().normalize();
				if (!Files.exists(path))
					Files.createDirectory(path);
				String fileName =
					"profile." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
				Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
				user.setProfileUrl("/images/profile/" + user.getId() + "/" + fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("profileUpload: {}, id: {}", e.getMessage(), user.getId());
			throw new ApiException(Error.IMAGE_UPLOAD);
		}

		user = userRepository.save(user);

		return new UserDto(user);
	}

	@Transactional
	public void delete(String id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ApiException(Error.NOT_EXIST_USER));
		user.delete();

		refreshTokenRepository.deleteById(user.getId());

		// 탈퇴한 유저 accessToken 접근 막는건 추가 개발 필요.

		userRepository.save(user);
	}

	private User findById(String id) {
		return userRepository.findById(id).orElseThrow(() -> new ApiException(Error.NOT_EXIST_USER));
	}
}
