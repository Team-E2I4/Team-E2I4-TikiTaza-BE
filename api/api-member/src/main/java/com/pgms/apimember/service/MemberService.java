package com.pgms.apimember.service;

import static com.pgms.coredomain.domain.common.MemberErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.MemberSignUpRequest;
import com.pgms.apimember.dto.response.MemberResponse;
import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public Long signUp(MemberSignUpRequest request) {
		validateDuplicateEmail(request);
		validateNewPassword(request.password(), request.passwordConfirm());
		Member member = request.toEntity(passwordEncoder.encode(request.password()));

		return memberRepository.save(member)
			.getId();
	}

	@Transactional(readOnly = true)
	public MemberResponse getMyProfileInfo(Long memberId) {
		Member member = getMember(memberId);
		return MemberResponse.from(member);
	}

	public void deleteMemberAccount(Long memberId) {
		Member member = getMember(memberId);
		member.delete();
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

	private void validateDuplicateEmail(MemberSignUpRequest request) {
		if (Boolean.TRUE.equals(memberRepository.existsByEmail(request.email()))) {
			throw new MemberException(DUPLICATE_MEMBER_EMAIL);
		}
	}

	private void validateNewPassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(PASSWORD_CONFIRM_NOT_MATCHED);
		}
	}
}
