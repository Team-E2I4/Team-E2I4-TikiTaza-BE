package com.pgms.api.socket.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pgms.api.domain.game.dto.response.GameInfoUpdateResponse;
import com.pgms.api.domain.game.dto.response.GameQuestionGetResponse;
import com.pgms.api.domain.game.dto.response.GameRoomGetResponse;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.util.Utils;
import com.pgms.coreinfrakafka.kafka.KafkaMessage;

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
	private MessageType type;                                 // 메시지 타입
	private Long roomId;                                      // 게임방 아이디
	private Long exitMemberId;                                // 나간 유저
	private GameRoomGetResponse roomInfo;                     // 게임방 정보
	private GameInfoUpdateResponse gameScore;                  // 게임 점수
	private List<GameRoomMemberGetResponse> allMembers;       // 해당 방에 본인을 포함한 전체 유저
	private List<GameQuestionGetResponse> questions;          // 게임 질문 리스트

	public String toJson() {
		return Utils.getString(this);
	}

	public KafkaMessage convertToKafkaMessage(String destination) {
		return new KafkaMessage(destination, toJson());
	}
}
