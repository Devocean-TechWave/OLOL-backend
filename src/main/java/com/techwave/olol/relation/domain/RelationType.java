package com.techwave.olol.relation.domain;

import lombok.Getter;

@Getter
public enum RelationType {
	GRAND_FATHER("할아버지", "GRAND_FATHER"),
	GRAND_MOTHER("할머니", "GRAND_MOTHER"),
	FATHER("아버지", "FATHER"),
	MOTHER("어머니", "MOTHER"),
	OLDER_BROTHER("형", "OLDER_BROTHER"),
	OLDER_SISTER("누나", "OLDER_SISTER"),
	YOUNGER_BROTHER("남동생", "YOUNGER_BROTHER"),
	YOUNGER_SISTER("여동생", "YOUNGER_SISTER"),
	SON("아들", "SON"),
	DAUGHTER("딸", "DAUGHTER"),
	GRAND_SON("손자", "GRAND_SON"),
	GRAND_DAUGHTER("손녀", "GRAND_DAUGHTER"),
	HUSBAND("남편", "HUSBAND"),
	WIFE("아내", "WIFE"),
	COUSIN("사촌", "COUSIN"), //사촌
	FRIEND("친구", "FRIEND"),
	NEIGHBOR("이웃", "NEIGHBOR"),
	SENIOR("선배", "SENIOR"), //선배
	JUNIOR("후배", "JUNIOR"), //후배
	DOG("강아지", "DOG"),
	CAT("고양이", "CAT"),
	ETC("기타", "ETC");

	private final String koreanName;
	private final String englishName;

	RelationType(String koreanName, String englishName) {
		this.koreanName = koreanName;
		this.englishName = englishName;
	}
}
