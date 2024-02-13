package com.pgms.api.socket.dto.response;

import com.pgms.coredomain.domain.member.Member;

public record GameRoomExitResponse(
	Long memberId,
	String nickname
) {
	public static GameRoomExitResponse from(Member member) {
		return new GameRoomExitResponse(
			member.getId(),
			member.getNickname()
		);
	}
}
