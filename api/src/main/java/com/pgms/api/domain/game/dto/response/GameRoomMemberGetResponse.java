package com.pgms.api.domain.game.dto.response;

import com.pgms.coredomain.domain.game.GameRoomMember;

public record GameRoomMemberGetResponse(
	Long memberId,
	String nickname,
	int ranking,
	boolean readyStatus
) {
	public static GameRoomMemberGetResponse from(GameRoomMember gameRoomMember, int ranking) {
		return new GameRoomMemberGetResponse(
			gameRoomMember.getMemberId(),
			gameRoomMember.getNickname(),
			ranking,
			gameRoomMember.isReadyStatus()
		);
	}
}
