package com.pgms.apisocket.dto.response;

import com.pgms.coredomain.domain.member.Member;

public record GameRoomEnterResponse(
	Long memberId,
	String nickname
) {
	public static GameRoomEnterResponse from(Member member) {
		return new GameRoomEnterResponse(
			member.getId(),
			member.getNickname()
		);
	}
}
