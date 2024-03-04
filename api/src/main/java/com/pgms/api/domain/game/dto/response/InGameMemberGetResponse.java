package com.pgms.api.domain.game.dto.response;

import com.pgms.coredomain.domain.game.GameRoomMember;

public record InGameMemberGetResponse(
	Long memberId,
	String nickname,
	boolean readyStatus
) {
	public static InGameMemberGetResponse from(GameRoomMember gameRoomMember) {
		return new InGameMemberGetResponse(
			gameRoomMember.getMemberId(),
			gameRoomMember.getNickname(),
			gameRoomMember.isReadyStatus()
		);
	}
}
