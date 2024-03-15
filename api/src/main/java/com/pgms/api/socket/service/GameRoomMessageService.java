package com.pgms.api.socket.service;

import static com.pgms.api.socket.dto.response.GameRoomMessageType.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgms.api.domain.game.dto.response.GameRoomGetResponse;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.socket.dto.response.GameRoomMessage;
import com.pgms.api.socket.dto.response.GameRoomMessageType;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coreinfrakafka.kafka.KafkaMessage;
import com.pgms.coreinfrakafka.kafka.producer.Producer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GameRoomMessageService {

	private static final String GAME_ROOM_MESSAGE_DESTINATION = "/from/game-room/";
	private final Producer producer;

	public void sendGameRoomInfoMessage(GameRoomMessageType type, GameRoom gameRoom,
		List<GameRoomMemberGetResponse> gameRoomMembers) {
		Long roomId = gameRoom.getId();
		KafkaMessage message = GameRoomMessage.builder()
			.type(type)
			.roomId(roomId)
			.roomInfo(GameRoomGetResponse.from(gameRoom))
			.allMembers(gameRoomMembers)
			.build()
			.convertToKafkaMessage(GAME_ROOM_MESSAGE_DESTINATION + roomId);

		producer.produceMessage(message);
	}

	public void sendExitGameRoomMessage(GameRoom gameRoom, Long exitId,
		List<GameRoomMemberGetResponse> leftGameRoomMember) {
		Long roomId = gameRoom.getId();

		KafkaMessage message = GameRoomMessage.builder()
			.type(EXIT)
			.roomId(roomId)
			.roomInfo(GameRoomGetResponse.from(gameRoom))
			.exitMemberId(exitId)
			.allMembers(leftGameRoomMember)
			.build()
			.convertToKafkaMessage(GAME_ROOM_MESSAGE_DESTINATION + roomId);

		producer.produceMessage(message);
	}

	public void sendKickMessage(Long roomId, Long kickedId) {
		KafkaMessage message = GameRoomMessage.builder()
			.type(KICKED)
			.roomId(roomId)
			.exitMemberId(kickedId)
			.build()
			.convertToKafkaMessage(GAME_ROOM_MESSAGE_DESTINATION + roomId);

		producer.produceMessage(message);
	}

	public void sendGameStartOrFailMessage(GameRoomMessageType type, Long roomId) {
		KafkaMessage message = GameRoomMessage.builder()
			.type(type)
			.build()
			.convertToKafkaMessage(GAME_ROOM_MESSAGE_DESTINATION + roomId);

		producer.produceMessage(message);
	}
}
