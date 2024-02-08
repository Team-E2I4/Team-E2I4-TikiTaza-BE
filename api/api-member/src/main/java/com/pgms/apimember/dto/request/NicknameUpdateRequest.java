package com.pgms.apimember.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NicknameUpdateRequest(
	@NotBlank(message = "닉네임은 비어서는 안됩니다.")
	@Size(min = 2, max = 8, message = "닉네임은 최소 2자이상 8자 미만입니다.")
	String nickname
) {
}
