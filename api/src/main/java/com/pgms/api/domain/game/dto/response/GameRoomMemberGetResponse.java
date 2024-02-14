package com.pgms.api.domain.game.dto.response;

import com.pgms.coredomain.domain.game.GameRoomMember;

public record GameRoomMemberGetResponse(
	Long id,
	Long memberId,
	String nickname,
	boolean readyStatus
) {
	public static GameRoomMemberGetResponse from(GameRoomMember gameRoomMember) {
		return new GameRoomMemberGetResponse(
			gameRoomMember.getId(),
			gameRoomMember.getMemberId(),
			gameRoomMember.getNickname(),
			gameRoomMember.isReadyStatus()
		);
	}
}
