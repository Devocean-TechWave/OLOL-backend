package com.techwave.olol.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.user.constant.GenderType;
import com.techwave.olol.user.constant.UserStatus;
import com.techwave.olol.user.dto.request.KakaoJoinRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	private String name;

	@Column(unique = true)
	private String nickname;

	private String password;

	private String profileUrl;

	private LocalDate birth;

	@Enumerated(value = EnumType.STRING)
	private GenderType gender;

	@Enumerated(value = EnumType.STRING)
	private AuthType authType; // 일반 유저, kakao 로그인 유저 구분

	@Column(unique = true)
	private String snsId; // kakao 로그인 ID

	@Enumerated(value = EnumType.STRING)
	private UserStatus status;

	@CreatedDate
	private LocalDateTime createdTime;

	@LastModifiedDate
	private LocalDateTime updatedTime;

	@Builder
	public User(AuthType authType, String snsId) {
		this.profileUrl = "default_profile.PNG";
		this.authType = authType;
		this.snsId = snsId;
		this.status = UserStatus.NORMAL;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public void setKakaoUser(KakaoJoinRequest request) {
		this.name = request.getName();
		this.nickname = request.getNickname();
		this.birth = request.getBirth();
		this.gender = GenderType.MALE.getName().equals(request.getGender()) ? GenderType.MALE : GenderType.FEMALE;
	}

	public void delete() {
		this.nickname = "[탈퇴한 유저] " + System.currentTimeMillis();
		this.profileUrl = "";
		this.status = UserStatus.DELETE;
		this.snsId = null; // kakao 재가입 가능 처리
	}
}
