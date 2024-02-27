package com.pgms.api.domain.online.dto;

import java.util.Objects;

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
