package com.techwave.olol.user.model;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.common.BaseEntity;
import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.user.constant.GenderType;

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
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "nickname", unique = true)
	private String nickname;

	@Column(name = "password")
	private String password;

	@Column(name = "profile_url")
	private String profileUrl;

	@Column(name = "birth")
	private LocalDate birth;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender")
	private GenderType gender;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "auth_type")
	private AuthType authType; // 일반 유저, kakao 로그인 유저 구분

	@Column(name = "is_delete")
	private boolean isDelete;

	@Column(name = "snsid", unique = true)
	private String snsId; // kakao 로그인 ID

	@Builder
	public User(AuthType authType, String snsId) {
		this.profileUrl = "default_profile.PNG";
		this.authType = authType;
		this.snsId = snsId;
		this.isDelete = false;
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
		this.isDelete = true;
		this.snsId = null; // kakao 재가입 가능 처리
	}
}
