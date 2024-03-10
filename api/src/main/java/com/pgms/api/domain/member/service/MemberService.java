package com.pgms.api.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.member.dto.request.MemberSignUpRequest;
import com.pgms.api.domain.member.dto.request.NicknameUpdateRequest;
import com.pgms.api.domain.member.dto.response.AccountGetResponse;
import com.pgms.api.domain.member.dto.response.MemberSignUpResponse;
import com.pgms.api.global.exception.MemberException;
import com.pgms.coredomain.domain.game.GameRank;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.exception.MemberErrorCode;
import com.pgms.coredomain.repository.GameRankRepository;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.entity.Guest;
import com.pgms.coreinfraredis.repository.GuestRepository;
import com.pgms.coreinfraredis.repository.RedisKeyRepository;
import com.pgms.coresecurity.resolver.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final GuestRepository guestRepository;
	private final GameRankRepository gameRankRepository;
	private final RedisKeyRepository redisKeyRepository;
	private final PasswordEncoder passwordEncoder;

	public MemberSignUpResponse signUp(MemberSignUpRequest request) {
		validateDuplicateEmail(request);
		validateNewPassword(request.password(), request.passwordConfirm());
		Member member = request.toEntity(passwordEncoder.encode(request.password()));

		return MemberSignUpResponse.from(memberRepository.save(member).getId());
	}

	@Transactional(readOnly = true)
	public AccountGetResponse getMyProfileInfo(Account account) {
		int rank = gameRankRepository.findTotalRanking(account.id())
			.map(GameRank::getRanking)
			.orElse(-1);

		if (account.isGuest()) {
			final Guest guest = guestRepository.findById(account.id())
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
			return AccountGetResponse.from(guest, rank);
		} else {
			final Member member = getMember(account.id());
			return AccountGetResponse.from(member, rank);
		}
	}

	public void deleteMemberAccount(String accessToken, String refreshToken, Long memberId) {
		final Member member = getMember(memberId);
		if (member.isDeleted()) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.delete();
		setAccessTokenToBlackList(accessToken, refreshToken, memberId);
	}

	public void updateMemberNickname(Long memberId, NicknameUpdateRequest request) {
		validateDuplicateNickname(request.nickname());
		Member member = getMember(memberId);
		member.updateNickname(request.nickname());
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	private void validateDuplicateEmail(MemberSignUpRequest request) {
		if (Boolean.TRUE.equals(memberRepository.existsByEmail(request.email()))) {
			throw new MemberException(MemberErrorCode.DUPLICATE_MEMBER_EMAIL);
		}
	}

	private void validateNewPassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(MemberErrorCode.PASSWORD_CONFIRM_NOT_MATCHED);
		}
	}

	private void validateDuplicateNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new MemberException(MemberErrorCode.DUPLICATE_NICKNAME);
		}
	}

	private void setAccessTokenToBlackList(String accessToken, String refreshToken, Long memberId) {
		if (redisKeyRepository.hasKey(refreshToken)) {
			Long storedMemberId = Long.valueOf(redisKeyRepository.get(refreshToken).toString());
			if (storedMemberId.equals(memberId)) {
				redisKeyRepository.delete(refreshToken);
				redisKeyRepository.saveBlackList(accessToken, "accessToken");
			}
		}
	}
}
