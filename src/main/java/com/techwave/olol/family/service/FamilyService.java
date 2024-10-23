package com.techwave.olol.family.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techwave.olol.auth.exception.AuthErrorCode;
import com.techwave.olol.auth.exception.AuthException;
import com.techwave.olol.family.domain.Family;
import com.techwave.olol.family.dto.FamilyExistDto;
import com.techwave.olol.family.dto.FamilyInfoDto;
import com.techwave.olol.family.dto.FamilyReqDto;
import com.techwave.olol.family.exception.FamilyErrorCode;
import com.techwave.olol.family.exception.FamilyException;
import com.techwave.olol.family.repository.FamilyRepository;
import com.techwave.olol.user.domain.User;
import com.techwave.olol.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FamilyService {
	private final FamilyRepository familyRepository;
	private final UserRepository userRepository;

	public FamilyExistDto existFamily(String familyName) {
		Optional<Family> familyOptional = familyRepository.findByName(familyName);
		return familyOptional.map(family -> new FamilyExistDto(true, family.getId()))
			.orElseGet(() -> new FamilyExistDto(false, null));
	}

	@Transactional
	public FamilyInfoDto createFamily(String userId, FamilyReqDto reqDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_EXIST));
		familyRepository.findByName(reqDto.getFamilyName())
			.ifPresent(family -> {
				throw new FamilyException(FamilyErrorCode.FAMILY_ALREADY_EXIST);
			});
		Family family = Family.builder().name(reqDto.getFamilyName()).build();
		user.setRole(reqDto.getMyRole());
		User savedUser = userRepository.save(user);
		family.addUser(savedUser);
		Family savedFamily = familyRepository.save(family);
		return FamilyInfoDto.fromEntity(savedFamily);
	}

	@Transactional
	public FamilyInfoDto joinFamily(String userId, FamilyReqDto reqDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_EXIST));
		Family family = familyRepository.findByName(reqDto.getFamilyName())
			.orElseThrow(() -> new FamilyException(FamilyErrorCode.FAMILY_NOT_EXIST));
		user.setRole(reqDto.getMyRole());
		User savedUser = userRepository.save(user);
		family.addUser(savedUser);
		Family savedFamily = familyRepository.save(family);
		return FamilyInfoDto.fromEntity(savedFamily);
	}

	@Transactional(readOnly = true)
	public FamilyInfoDto getFamilyInfo(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_EXIST));
		Family family = user.getFamily();
		if (family == null) {
			throw new FamilyException(FamilyErrorCode.NO_FAMILY);
		}
		return FamilyInfoDto.fromEntity(family);
	}

	@Transactional(readOnly = true)
	public List<FamilyInfoDto> getFamilyRank() {
		// 점수 기준으로 내림차순으로 정렬된 가족 목록 가져오기
		List<Family> families = familyRepository.findAllByOrderByScoreDesc();

		// 순위 계산을 위한 변수를 추가
		List<FamilyInfoDto> rankedFamilies = new ArrayList<>();
		long currentRank = 1;
		long previousScore = -1;
		long sameRankCounter = 0;

		for (int i = 0; i < families.size(); i++) {
			Family family = families.get(i);
			FamilyInfoDto familyInfoDto = FamilyInfoDto.fromEntity(family);

			// 같은 점수일 때는 같은 순위를 부여하고, 점수가 달라지면 순위를 업데이트
			if (family.getScore().equals(previousScore)) {
				familyInfoDto.setRanking(currentRank);
				sameRankCounter++;
			} else {
				currentRank += sameRankCounter;
				familyInfoDto.setRanking(currentRank);
				sameRankCounter = 1; // 새로운 점수 그룹이므로 다시 초기화
			}

			// 현재 가족의 점수를 이전 점수로 설정
			previousScore = family.getScore();

			rankedFamilies.add(familyInfoDto);
		}

		return rankedFamilies;
	}
}
