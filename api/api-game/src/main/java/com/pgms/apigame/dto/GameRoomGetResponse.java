package com.pgms.apigame.dto;

import com.pgms.coredomain.domain.game.GameRoom;

public record GameRoomGetResponse(
	Long id,
	Long ownerId,
	String title,
	String inviteCode,
	Integer maxPlayer,
	Integer currentPlayer,
	boolean isStarted,
	boolean isPrivate

) {

	public static GameRoomGetResponse from(GameRoom gameRoom) {
		return new GameRoomGetResponse(
			gameRoom.getId(),
			gameRoom.getOwnerId(),
			gameRoom.getTitle(),
			gameRoom.getInviteCode(),
			gameRoom.getMaxPlayer(),
			gameRoom.getCurrentPlayer(),
			gameRoom.isStarted(),
			gameRoom.isPrivate()
		);
	}
}
