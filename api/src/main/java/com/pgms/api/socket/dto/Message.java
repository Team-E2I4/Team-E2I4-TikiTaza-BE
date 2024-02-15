package com.pgms.api.socket.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.util.Utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
	private Long senderId;        // 보내는 유저 UUID
	private MessageType type;    // 메시지 타입
	private Long room;            // roomId
	private List<GameRoomMemberGetResponse> otherMembers;    // 해당 방에 본인을 제외한 전체 유저
	private List<GameRoomMemberGetResponse> allMembers;        // 해당 방에 본인을 포함한 전체 유저
	private Long exitMemberId;        // 나간 유저

	public String toJson() {
		return Utils.getString(this);
	}
}