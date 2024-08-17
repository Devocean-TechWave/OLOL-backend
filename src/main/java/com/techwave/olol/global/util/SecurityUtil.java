package com.techwave.olol.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.techwave.olol.user.dto.SecurityUser;

public class SecurityUtil {

	private SecurityUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static UserDetails getCurrentUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			return (UserDetails)authentication.getPrincipal();
		}
		throw new IllegalStateException("User not authenticated");
	}

	public static String getCurrentUserId() {
		UserDetails userDetails = getCurrentUserDetails();
		if (userDetails instanceof SecurityUser) {  // Assuming SecurityUser extends UserDetails
			return ((SecurityUser)userDetails).getUsername();
		}
		throw new IllegalStateException("UserDetails does not contain userId");
	}
}

