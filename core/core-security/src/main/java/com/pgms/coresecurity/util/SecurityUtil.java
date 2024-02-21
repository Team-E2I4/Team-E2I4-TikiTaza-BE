package com.pgms.coresecurity.util;

import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pgms.coredomain.exception.SecurityErrorCode;
import com.pgms.coresecurity.exception.SecurityException;
import com.pgms.coresecurity.user.normal.UserDetailsImpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

	public static Long getCurrentAccountId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		validateAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		return userDetails.getId();
	}

	private static void validateAuthentication(Authentication authentication) {
		if (Objects.isNull(authentication) || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new SecurityException(SecurityErrorCode.UNAUTHORIZED);
		}
	}
}
