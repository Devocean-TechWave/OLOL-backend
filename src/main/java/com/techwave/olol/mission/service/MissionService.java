package com.techwave.olol.mission.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;
import com.techwave.olol.global.exception.GlobalCodeException;
import com.techwave.olol.global.exception.GlobalErrorCode;
import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.mission.dto.MemoryResDto;
import com.techwave.olol.mission.dto.MissionResDto;
import com.techwave.olol.mission.repository.MemoryRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionService {

	@Value("${cloud.aws.bucket.name}")
	private String bucketName;

	private final AmazonS3 amazonS3Client;
	// private final MissionRepository missionRepository;
	private final UserRepository userRepository;
	private final MemoryRepository memoryRepository;

	public void createTheme(String imageUrl) {

	}

	public void verifyMission(String userId, UUID missionId, MultipartFile multipartFile) throws IOException {
		// 유저 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + userId));
		if (user.getFamily() == null) {
			throw new AuthException(AuthErrorCode.NO_FAMILY);
		}
		// 미션 조회
		// Mission mission = missionRepository.findById(missionId)
		// 	.orElseThrow(() -> new IllegalArgumentException("해당 ID의 미션을 찾을 수 없습니다: " + missionId));

		// 파일 타입 체크
		if (!isValidFileType(multipartFile)) {
			throw new GlobalCodeException(GlobalErrorCode.FILE_ONLY_IMAGE);
		}

		// 이미지 저장 및 URL 반환
		// String uploadImageUrl = saveImage(multipartFile, mission.getId());
		//
		// Memory memory = Memory.builder()
		// 	.mission(mission)
		// 	.imageUrl(uploadImageUrl)
		// 	.successDate(LocalDate.now())
		// 	.build();
		//
		// memoryRepository.save(memory);
		// missionRepository.save(mission);
	}

	// 미션 조회
	public List<Mission> getMissions(String userId, boolean active, boolean isGiver) {
		// 유저 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + userId));

		// // 유저가 생성한 모든 미션 조회
		// if (isGiver) {
		// 	return active
		// 		? missionRepository.findProgressMissionsByGiver(user)
		// 		: missionRepository.findCompletedMissionsByGiver(user);
		// } else {
		// 	return active
		// 		? missionRepository.findProgressMissionsByReceiver(user)
		// 		: missionRepository.findCompletedMissionsByReceiver(user);
		// }
		return null;
	}

	public MissionResDto getTodayMission(String userId) {
		// 유저 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + userId));

		// LocalDate today = LocalDate.now();
		// List<Mission> missions = missionRepository.findTodayMission(user, today);
		// if (missions.isEmpty()) {
		// 	return null;
		// }
		// Mission mission = missions.get(0);
		// return MissionResDto.builder()
		// 	.missionId(mission.getId())
		// 	.missionName(mission.getName())
		// 	.isComplete(mission.isComplete())
		// 	.build();
		return null;
	}

	public List<MemoryResDto> getSuccessMission(String userId, boolean all) {
		// 유저 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + userId));

		// List<Mission> missions = missionRepository.findSuccessMissions(user);
		// return missions.stream()
		// 	.map(mission -> MissionResDto.builder()
		// 		.missionId(mission.getId())
		// 		.missionName(mission.getName())
		// 		.isComplete(mission.isComplete())
		// 		.build())
		// 	.collect(Collectors.toList());
		return null;
	}

	public List<MemoryResDto> getSuccessMission(String userId, Long familyId) {
		// 유저 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저를 찾을 수 없습니다: " + userId));

		// List<Mission> missions = missionRepository.findSuccessMissions(user, familyId);
		// return missions.stream()
		// 	.map(mission -> MissionResDto.builder()
		// 		.missionId(mission.getId())
		// 		.missionName(mission.getName())
		// 		.isComplete(mission.isComplete())
		// 		.build())
		// 	.collect(Collectors.toList());
		return null;
	}

	// === 편의 메서드 ===

	public String saveImage(MultipartFile multipartFile, UUID missionId) throws IOException {
		// 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
		String originalFileName = multipartFile.getOriginalFilename();

		// 미션 ID를 파일명에 추가
		String uniqueFileName = missionId.toString() + "_" + originalFileName.replaceAll("\\s", "_");

		String fileName = "missions/" + uniqueFileName; // S3에서 파일이 저장될 경로
		log.info("fileName: " + fileName);

		File uploadFile = convert(multipartFile);

		String uploadImageUrl = putS3(uploadFile, fileName);
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}

	private boolean isValidFileType(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && (contentType.startsWith("image/"));
	}

	private File convert(MultipartFile file) throws IOException {
		String originalFileName = file.getOriginalFilename();
		String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("\\s", "_");

		File convertFile = new File(uniqueFileName);
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(file.getBytes());
			} catch (IOException e) {
				log.error("파일 변환 중 오류 발생: {}", e.getMessage());
				throw e;
			}
			return convertFile;
		}
		throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
	}

	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("파일이 삭제되었습니다.");
		} else {
			log.info("파일이 삭제되지 못했습니다.");
		}
	}

	private String putS3(File uploadFile, String fileName) {
		amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile));
		return amazonS3Client.getUrl(bucketName, fileName).toString();
	}

}
