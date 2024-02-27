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
import com.pgms.api.global.exception.MemberException;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.Role;
import com.pgms.coredomain.exception.MemberErrorCode;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.entity.Guest;
import com.pgms.coreinfraredis.repository.GuestRepository;
import com.pgms.coreinfraredis.repository.RedisRepository;
import com.pgms.coresecurity.jwt.JwtTokenProvider;
import com.pgms.coresecurity.user.normal.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
	private static final Long STARTING_GUEST_ID = 10000L;

	private final AuthenticationManager authenticationManager;
	private final RedisRepository redisRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final GuestRepository guestRepository;

	public AuthResponse login(LoginRequest request) {
		validateMember(request.email());

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			request.email(),
			request.password()
		);

		Authentication authenticate = authenticationManager.authenticate(authentication);

		String accessToken = jwtTokenProvider.createAccessToken((UserDetailsImpl)authenticate.getPrincipal());
		String refreshToken = jwtTokenProvider.createRefreshToken();

		redisRepository.saveRefreshToken(refreshToken, ((UserDetailsImpl)authenticate.getPrincipal()).getId());
		return AuthResponse.from(accessToken, refreshToken);
	}

	public AuthResponse guestLogin() {
		final Long num = redisRepository.guestIdIncrement("guestCount");
		final Long guestId = STARTING_GUEST_ID + num;
		final String uuid = UUID.randomUUID().toString();
		final Guest guest = new Guest(guestId, "손님" + uuid.substring(0, 5));

		guestRepository.save(guest);

		String accessToken = jwtTokenProvider.createAccessToken(createGuestDetails(guest));
		String refreshToken = jwtTokenProvider.createRefreshToken();

		redisRepository.saveRefreshToken(refreshToken, guest.getId());
		return AuthResponse.from(accessToken, refreshToken);
	}

	public void logout(String accessToken, String refreshToken, Long accountId) {
		if (redisRepository.hasKey(refreshToken)) {
			Long storedAccountId = Long.valueOf(redisRepository.get(refreshToken).toString());
			if (storedAccountId.equals(accountId)) {
				redisRepository.delete(refreshToken);
				redisRepository.saveBlackList(accessToken, "accessToken");
			}
		}
	}

	public AuthResponse reIssueAccessTokenByRefresh(String refreshToken) {
		Long accountId = Long.valueOf(redisRepository.get(refreshToken).toString());

		// Guest 조회
		Guest guest = guestRepository.findById(accountId).orElse(null);
		if (guest != null) {
			return createAndSaveTokens(createGuestDetails(guest), refreshToken);
		}

		// Member 조회
		Member member = memberRepository.findById(accountId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
		return createAndSaveTokens(createUserDetails(member), refreshToken);
	}

	private AuthResponse createAndSaveTokens(UserDetailsImpl userDetails, String refreshToken) {
		String newAccessToken = jwtTokenProvider.createAccessToken(userDetails);
		String newRefreshToken = jwtTokenProvider.createRefreshToken();
		redisRepository.saveRefreshToken(newRefreshToken, userDetails.getId());
		redisRepository.delete(refreshToken);
		return AuthResponse.from(newAccessToken, newRefreshToken);
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
			.nickname(member.getNickname())
			.authorities(authorities)
			.build();
	}

	private UserDetailsImpl createGuestDetails(Guest guest) {
		Collection<? extends GrantedAuthority> authorities = List.of(
			new SimpleGrantedAuthority(Role.ROLE_GUEST.name()));

		return UserDetailsImpl.builder()
			.id(guest.getId())
			.nickname(guest.getNickname())
			.authorities(authorities)
			.build();
	}
}
