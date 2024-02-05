package com.pgms.apimember.dto.request;

import com.pgms.coredomain.domain.member.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberSignUpRequest(
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email(message = "유효하지 않은 이메일 형식입니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수 항목입니다.")
	@Size(min = 6, max = 15, message = "비밀번호는 최소 6자 이상, 최대 15자 이하입니다.")
	String password,

	@NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
	String passwordConfirm
) {
	public Member toEntity(String encodedPassword) {
		return Member.builder()
			.email(email)
			.password(encodedPassword)
			.build();
	}
}
