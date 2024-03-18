package com.pgms.api.socket.service;

import static com.pgms.api.socket.dto.response.GameMessageType.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pgms.api.domain.game.dto.response.GameQuestionGetResponse;
import com.pgms.api.domain.game.dto.response.InGameMemberGetResponse;
import com.pgms.api.socket.dto.response.GameMessage;
import com.pgms.api.socket.dto.response.GameMessageType;
import com.pgms.coreinfrakafka.kafka.KafkaMessage;
import com.pgms.coreinfrakafka.kafka.producer.Producer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GameMessageService {

	private static final String GAME_MESSAGE_DESTINATION = "/from/game/";
	private final Producer producer;

	public void sendQuestionsAndUserInfoMessage(Long roomId, List<GameQuestionGetResponse> questions,
		List<InGameMemberGetResponse> gameRoomMembers) {
		KafkaMessage message = GameMessage.builder()
			.type(FIRST_ROUND_START)
			.questions(questions)
			.allMembers(gameRoomMembers)
			.build()
			.convertToKafkaMessage(GAME_MESSAGE_DESTINATION + roomId);
		producer.produceMessage(message);
	}

	public void sendGameInfoMessage(Long roomId, List<InGameMemberGetResponse> allMembers) {
		KafkaMessage message = GameMessage.builder()
			.type(GameMessageType.INFO)
			.allMembers(allMembers)
			.build()
			.convertToKafkaMessage(GAME_MESSAGE_DESTINATION + roomId);
		producer.produceMessage(message);
	}

	public void sendWordGameInfoMessage(Long roomId, Long accountId, String submittedWord,
		List<GameQuestionGetResponse> remainWords,
		List<InGameMemberGetResponse> allMembers) {

		KafkaMessage message = GameMessage.builder()
			.type(INFO)
			.submittedWord(submittedWord)
			.submitMemberId(accountId)
			.questions(remainWords)
			.allMembers(allMembers)
			.build()
			.convertToKafkaMessage(GAME_MESSAGE_DESTINATION + roomId);
		producer.produceMessage(message);
	}

	public void sendNextRoundMessage(Long roomId, List<InGameMemberGetResponse> allMembers,
		List<GameQuestionGetResponse> questionResponses) {
		KafkaMessage message = GameMessage.builder()
			.type(NEXT_ROUND_START)
			.allMembers(allMembers)
			.questions(questionResponses)
			.build()
			.convertToKafkaMessage(GAME_MESSAGE_DESTINATION + roomId);
		producer.produceMessage(message);
	}

	public void sendFinishGameMessage(Long roomId, List<InGameMemberGetResponse> allMembers) {
		KafkaMessage message = GameMessage.builder()
			.type(GameMessageType.FINISH)
			.allMembers(allMembers)
			.build()
			.convertToKafkaMessage(GAME_MESSAGE_DESTINATION + roomId);
		producer.produceMessage(message);
	}
}
