package com.pgms.apimember.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pgms.apimember.dto.request.MemberSignUpRequest;
import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.repository.MemberRepository;

import jakarta.transaction.Transactional;
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

	private void validateDuplicateEmail(MemberSignUpRequest request) {
		if (memberRepository.existsByEmail(request.email())) {
			throw new MemberException(MemberErrorCode.DUPLICATE_MEMBER_EMAIL);
		}
	}

	private void validateNewPassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(MemberErrorCode.PASSWORD_CONFIRM_NOT_MATCHED);
		}
	}
}

