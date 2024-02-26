package com.pgms.coresecurity.resolver;

import static com.pgms.coredomain.domain.member.Role.*;

public record Account(
	Long id,
	String nickname,
	boolean isGuest
) {
	public static Account of(Long id, String nickname, String role) {
		return new Account(id, nickname, ROLE_GUEST.name().equals(role));
	}
}
