package com.pgms.api.domain.game.dto.response;

import com.pgms.coredomain.domain.game.GameRoom;

public record GameRoomGetResponse(
	Long id,
	Long hostId,
	String title,
	String inviteCode,
	Integer maxPlayer,
	Integer currentPlayer,
	boolean isPlaying,
	boolean isPrivate

) {

	public static GameRoomGetResponse from(GameRoom gameRoom) {
		return new GameRoomGetResponse(
			gameRoom.getId(),
			gameRoom.getHostId(),
			gameRoom.getTitle(),
			gameRoom.getInviteCode(),
			gameRoom.getMaxPlayer(),
			gameRoom.getCurrentPlayer(),
			gameRoom.isPlaying(),
			gameRoom.isPrivate()
		);
	}
}
