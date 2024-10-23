package com.techwave.olol.user.domain;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.family.domain.Family;
import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.user.dto.request.KakaoJoinRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity(name = "users")
@Builder
@AllArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Setter
	@Column(name = "nickname", unique = true)
	private String nickname;

	@Column(name = "password")
	private String password;

	@Setter
	@Column(name = "profile_url")
	private String profileUrl;

	@Column(name = "birth")
	private LocalDate birth;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender")
	private GenderType gender;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "auth_type", nullable = false)
	private AuthType authType; // 일반 유저, kakao 로그인 유저 구분

	@Column(name = "sns_id", unique = true, nullable = false)
	private String snsId; // kakao 로그인 ID

	@Column(name = "role")
	private String role;

	@Column(name = "is_delete")
	@ColumnDefault("false")
	@Builder.Default
	private Boolean isDelete = false;

	@ManyToOne
	@JoinColumn(name = "family_id")
	private Family family;

	@Builder
	public User(AuthType authType, String snsId) {
		this.profileUrl = "default_profile.png";
		this.authType = authType;
		this.snsId = snsId;
		this.isDelete = false;
	}

	public void setKakaoUser(KakaoJoinRequestDto request) {
		this.name = request.getName();
		this.nickname = request.getNickname();
		this.birth = request.getBirth();
		this.gender = request.getGender();
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

}
