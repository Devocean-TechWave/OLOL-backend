package com.techwave.olol.user.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

@Data
public class UserPrincipal implements UserDetails {

	private final String id;
	private final String password;
	private final Collection<GrantedAuthority> authorities;
	private Map<String, Object> attributes;

	@Override
	public String getUsername() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Builder
	public UserPrincipal(User user, Map<String, Object> attributes) {
		this.id = String.valueOf(user.getId());
		this.password = user.getPassword();
		this.authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
		if (attributes != null) {
			this.attributes = attributes;
		}
	}
}
