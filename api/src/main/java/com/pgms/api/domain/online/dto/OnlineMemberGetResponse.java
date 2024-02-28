package com.pgms.api.domain.online.dto;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "실시간 접속자 응답", requiredProperties = {"memberId", "nickname"})
public record OnlineMemberGetResponse(
	Long memberId,
	String nickname
) {
	public static OnlineMemberGetResponse of(Long memberId, String nickname) {
		return new OnlineMemberGetResponse(memberId, nickname);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OnlineMemberGetResponse that = (OnlineMemberGetResponse)o;
		return Objects.equals(memberId, that.memberId) && Objects.equals(nickname, that.nickname);
	}

	@Override
	public int hashCode() {
		return Objects.hash(memberId, nickname);
	}
}
