package com.pgms.api.domain.member.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.member.dto.request.LoginRequest;
import com.pgms.api.domain.member.dto.response.AuthResponse;
import com.pgms.api.exception.MemberException;
import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.Role;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.repository.RedisRepository;
import com.pgms.coresecurity.jwt.JwtTokenProvider;
import com.pgms.coresecurity.user.normal.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final RedisRepository redisRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;

	public AuthResponse login(LoginRequest request) {
		validateMember(request.email());

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			request.email(),
			request.password()
		);

		Authentication authenticate = authenticationManager.authenticate(authentication);

		String accessToken = jwtTokenProvider.createAccessToken((UserDetailsImpl)authenticate.getPrincipal());
		String refreshToken = jwtTokenProvider.createRefreshToken();

		redisRepository.save(refreshToken, ((UserDetailsImpl)authenticate.getPrincipal()).getId());
		return AuthResponse.from(accessToken, refreshToken);
	}

	public AuthResponse guestLogin() {
		String randomNickname = UUID.randomUUID().toString();
		Member member = Member.builder()
			.email("Guest@tikitaza.com")
			.nickname(randomNickname)
			.role(Role.ROLE_GUEST)
			.build();

		Member savedGuest = memberRepository.save(member);

		String accessToken = jwtTokenProvider.createAccessToken(createUserDetails(savedGuest));
		String refreshToken = jwtTokenProvider.createRefreshToken();

		redisRepository.save(refreshToken, savedGuest.getId());
		return AuthResponse.from(accessToken, refreshToken);
	}

	public void logout(String accessToken, String refreshToken, Long memberId) {
		if (redisRepository.hasKey(refreshToken)) {
			Long storedMemberId = Long.valueOf(redisRepository.get(refreshToken).toString());
			if (storedMemberId.equals(memberId)) {
				redisRepository.delete(refreshToken);
				redisRepository.saveBlackList(accessToken, "accessToken");
			}
		}
	}

	public AuthResponse reIssueAccessTokenByRefresh(String token) {
		Long memberId = Long.valueOf(redisRepository.get(token).toString());

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		UserDetailsImpl userDetails = createUserDetails(member);
		String accessToken = jwtTokenProvider.createAccessToken(userDetails);
		String refreshToken = jwtTokenProvider.createRefreshToken();

		return AuthResponse.from(accessToken, refreshToken);
	}

	private void validateMember(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		if (member.isDeleted()) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
	}

	private UserDetailsImpl createUserDetails(Member member) {
		Collection<? extends GrantedAuthority> authorities = List.of(
			new SimpleGrantedAuthority(member.getRole().name()));

		return UserDetailsImpl.builder()
			.id(member.getId())
			.authorities(authorities)
			.build();
	}
}
