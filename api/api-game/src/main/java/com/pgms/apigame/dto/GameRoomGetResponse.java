package com.pgms.apigame.dto;

import com.pgms.coredomain.domain.game.GameRoom;

public record GameRoomGetResponse(
	Long id,
	Long ownerId,
	String title,
	String inviteCode,
	Integer maxPlayers,
	Integer currentPlayers,
	boolean isStarted,
	boolean isPrivate

) {

	public static GameRoomGetResponse from(GameRoom gameRoom) {
		return new GameRoomGetResponse(
			gameRoom.getId(),
			gameRoom.getOwnerId(),
			gameRoom.getTitle(),
			gameRoom.getInviteCode(),
			gameRoom.getCurrentPlayer(),
			gameRoom.getMaxPlayer(),
			gameRoom.getIsStarted(),
			gameRoom.getPassword() != null
		);
	}
}
