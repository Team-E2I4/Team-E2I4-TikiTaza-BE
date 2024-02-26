package com.pgms.api.socket.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.global.util.Utils;
import com.pgms.coreinfrakafka.kafka.KafkaMessage;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Message {
	protected List<GameRoomMemberGetResponse> allMembers;       // 전체 유저 정보

	public String toJson() {
		return Utils.getString(this);
	}

	public KafkaMessage convertToKafkaMessage(String destination) {
		return new KafkaMessage(destination, toJson());
	}
}
