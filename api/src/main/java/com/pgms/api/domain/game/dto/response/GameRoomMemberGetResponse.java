package com.pgms.api.domain.game.dto.response;

import com.pgms.coredomain.domain.game.GameRoomMember;

public record GameRoomMemberGetResponse(
	Long memberId,
	String nickname,
	Long ranking,
	boolean readyStatus
) {
	public static GameRoomMemberGetResponse from(GameRoomMember gameRoomMember, Long ranking) {
		return new GameRoomMemberGetResponse(
			gameRoomMember.getMemberId(),
			gameRoomMember.getNickname(),
			ranking,
			gameRoomMember.isReadyStatus()
		);
	}
}
