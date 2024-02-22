package com.pgms.api.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.member.dto.request.MemberSignUpRequest;
import com.pgms.api.domain.member.dto.request.NicknameUpdateRequest;
import com.pgms.api.domain.member.dto.response.MemberGetResponse;
import com.pgms.api.global.exception.MemberException;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.exception.MemberErrorCode;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.repository.RedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RedisRepository redisRepository;

	public Long signUp(MemberSignUpRequest request) {
		validateDuplicateEmail(request);
		validateNewPassword(request.password(), request.passwordConfirm());
		Member member = request.toEntity(passwordEncoder.encode(request.password()));

		return memberRepository.save(member)
			.getId();
	}

	@Transactional(readOnly = true)
	public MemberGetResponse getMyProfileInfo(Long memberId) {
		Member member = getMember(memberId);
		return MemberGetResponse.from(member);
	}

	public void deleteMemberAccount(String accessToken, String refreshToken, Long memberId) {
		Member member = getMember(memberId);
		if (member.isDeleted()) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
		}
		member.delete();
		setAccessTokenToBlackList(accessToken, refreshToken, memberId);
	}

	public void updateMemberNickname(Long memberId, NicknameUpdateRequest request) {
		validateDuplicateNickname(request.nickname());
		Member member = getMember(memberId);
		member.setNickname(request.nickname());
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
		if (redisRepository.hasKey(refreshToken)) {
			Long storedMemberId = Long.valueOf(redisRepository.get(refreshToken).toString());
			if (storedMemberId.equals(memberId)) {
				redisRepository.delete(refreshToken);
				redisRepository.saveBlackList(accessToken, "accessToken");
			}
		}
	}
}
