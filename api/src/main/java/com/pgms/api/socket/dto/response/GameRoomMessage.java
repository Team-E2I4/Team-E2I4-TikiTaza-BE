package com.pgms.api.socket.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pgms.api.domain.game.dto.response.GameRoomGetResponse;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameRoomMessage extends Message {
	private GameRoomMessageType type;                  // 게임방 메시지 타입
	private Long roomId;                                      // 게임방 아이디 -> 게임방
	protected List<GameRoomMemberGetResponse> allMembers;       // 전체 유저 정보
	private Long exitMemberId;                                // 나간 유저 -> 게임방
	private GameRoomGetResponse roomInfo;                     // 게임방 정보 -> 게임방
}
