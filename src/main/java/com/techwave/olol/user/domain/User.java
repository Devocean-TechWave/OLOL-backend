package com.techwave.olol.user.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techwave.olol.cheer.domain.Cheer;
import com.techwave.olol.global.jpa.BaseEntity;
import com.techwave.olol.login.constant.AuthType;
import com.techwave.olol.mission.domain.Mission;
import com.techwave.olol.relation.domain.UserRelationShip;
import com.techwave.olol.user.dto.request.KakaoJoinRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity(name = "users")
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
	@Column(name = "gender", nullable = false)
	private GenderType gender;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "auth_type", nullable = false)
	private AuthType authType; // 일반 유저, kakao 로그인 유저 구분

	@Column(name = "is_delete")
	@ColumnDefault("false")
	private Boolean isDelete;

	@Column(name = "sns_id", unique = true, nullable = false)
	private String snsId; // kakao 로그인 ID

	@OneToMany(mappedBy = "cheerGiver", orphanRemoval = true)
	private List<Cheer> cheers;

	@OneToMany(mappedBy = "giver", orphanRemoval = true)
	private List<Mission> givenMissions;

	@OneToMany(mappedBy = "receiver")
	private List<Mission> receivedMissions;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserRelationShip> sentRequests = new HashSet<>();

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserRelationShip> receivedRequests = new HashSet<>();

	@Builder
	public User(AuthType authType, String snsId) {
		this.profileUrl = "default_profile.PNG";
		this.authType = authType;
		this.snsId = snsId;
		this.isDelete = false;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setKakaoUser(KakaoJoinRequest request) {
		this.name = request.getName();
		this.nickname = request.getNickname();
		this.birth = request.getBirth();
		this.gender = GenderType.MALE.getName().equals(request.getGender()) ? GenderType.MALE : GenderType.FEMALE;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public void addSenderRelationShip(UserRelationShip relationShip) {
		sentRequests.add(relationShip);
	}

	public void addReceiverRelationShip(UserRelationShip relationShip) {
		receivedRequests.add(relationShip);
	}

}
