package com.pgms.api.domain.game.dto.response;

import com.pgms.coredomain.domain.game.GameRoomMember;

public record InGameMemberGetResponse(
	Long memberId,
	String nickname,
	boolean readyStatus,
	double score
) {
	public static InGameMemberGetResponse from(GameRoomMember gameRoomMember) {
		return new InGameMemberGetResponse(
			gameRoomMember.getMemberId(),
			gameRoomMember.getNickname(),
			gameRoomMember.isReadyStatus(),
			0.0
		);
	}

	public static InGameMemberGetResponse from(GameRoomMember gameRoomMember, double score) {
		return new InGameMemberGetResponse(
			gameRoomMember.getMemberId(),
			gameRoomMember.getNickname(),
			gameRoomMember.isReadyStatus(),
			score
		);
	}
}
