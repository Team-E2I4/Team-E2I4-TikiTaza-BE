package com.pgms.coresecurity.service;

import static com.pgms.coredomain.domain.common.MemberErrorCode.*;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coresecurity.exception.SecurityException;
import com.pgms.coresecurity.user.normal.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new SecurityException(MEMBER_NOT_FOUND));

		if (member.isDeleted()) {
			throw new SecurityException(MEMBER_ALREADY_DELETED);
		}
		List<GrantedAuthority> authorities = getAuthorities(member);

		return UserDetailsImpl.builder()
			.id(member.getId())
			.email(member.getEmail())
			.password(member.getPassword())
			.authorities(authorities)
			.build();
	}

	private List<GrantedAuthority> getAuthorities(Member member) {
		return member.getRole() != null ?
			List.of(new SimpleGrantedAuthority(member.getRole().name()))
			: null;
	}
}
