package com.pgms.api.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "게임방 초대코드에 따른 방번호 응답", requiredProperties = {"roomId"})
public record GameRoomInviteCodeResponse(Long roomId) {
	public static GameRoomInviteCodeResponse from(Long roomId) {
		return new GameRoomInviteCodeResponse(roomId);
	}
}
