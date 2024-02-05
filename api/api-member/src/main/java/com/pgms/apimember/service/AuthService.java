package com.pgms.apimember.service;

import static com.pgms.coredomain.domain.common.MemberErrorCode.*;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pgms.apimember.dto.request.LoginRequest;
import com.pgms.apimember.dto.response.AuthResponse;
import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.repository.RedisRepository;
import com.pgms.coresecurity.jwt.JwtTokenProvider;
import com.pgms.coresecurity.user.normal.UserDetailsImpl;

import jakarta.transaction.Transactional;
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
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			request.email(),
			request.password()
		);

		Authentication authenticate = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String accessToken = jwtTokenProvider.createAccessToken((UserDetailsImpl)authenticate.getPrincipal());
		String refreshToken = jwtTokenProvider.createRefreshToken();

		redisRepository.save(refreshToken, ((UserDetailsImpl)authenticate.getPrincipal()).getId().toString());
		return AuthResponse.from(accessToken, refreshToken);
	}

	public AuthResponse reIssueAccessTokenByRefresh(String token) {
		// TODO: Redis 관련 만료 예외 처리 해야 함
		Long memberId = redisRepository.getMemberId(token)
			.orElseThrow();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		UserDetailsImpl userDetails = createUserDetails(member);
		String accessToken = jwtTokenProvider.createAccessToken(userDetails);
		String refreshToken = jwtTokenProvider.createRefreshToken();

		return AuthResponse.from(accessToken, refreshToken);
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
