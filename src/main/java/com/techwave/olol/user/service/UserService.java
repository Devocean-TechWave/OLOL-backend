package com.techwave.olol.user.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.techwave.olol.global.exception.ApiException;
import com.techwave.olol.global.exception.Error;
import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.login.repository.RefreshTokenRepository;
import com.techwave.olol.user.domain.GenderType;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.dto.UserDto;
import com.techwave.olol.user.dto.request.EditUserRequest;
import com.techwave.olol.user.dto.request.KakaoJoinRequest;
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
		Optional<User> userOpt = userRepository.findByNickname(nickname);
		return userOpt.map(UserDto::new).orElse(null);
	}

	public boolean checkNickname(String nickname) {
		Optional<User> userOpt = userRepository.findByNickname(nickname);
		return userOpt.isEmpty();
	}

	// 성별 예외처리 코드 추가
	@Transactional
	public void kakaoJoin(String id, KakaoJoinRequest request) {
		User user = findById(id);
		if (user.getAuthType() != AuthType.KAKAO)
			throw new ApiException(Error.AUTH_TYPE_MISMATCH);

		if (!GenderType.MALE.getName().equalsIgnoreCase(request.getGender())
			&& !GenderType.FEMALE.getName().equalsIgnoreCase(request.getGender())) {
			throw new ApiException(Error.INVALID_DATA);
		}

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
		user.setIsDelete(true);  // 유저 삭제 상태로 설정
		refreshTokenRepository.deleteById(user.getId());
		userRepository.save(user);
	}

	private User findById(String id) {
		return userRepository.findById(id).orElseThrow(() -> new ApiException(Error.NOT_EXIST_USER));
	}
}
