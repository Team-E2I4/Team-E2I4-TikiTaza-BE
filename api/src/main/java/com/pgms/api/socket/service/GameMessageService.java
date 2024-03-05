package com.pgms.api.socket.service;

import static com.pgms.api.socket.dto.response.GameMessageType.*;

import java.util.List;
import java.util.Map;

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

	private final Producer producer;

	public void sendGameInfoMessage(Long roomId, Map<Long, Long> gameScores) {
		KafkaMessage message = GameMessage.builder()
			.type(GameMessageType.INFO)
			.gameScore(Map.copyOf(gameScores))
			.build()
			.convertToKafkaMessage("/from/game/%d".formatted(roomId));
		producer.produceMessage(message);
	}

	public void sendWordGameInfoMessage(Long roomId, Long accountId, String submittedWord, Map<Long, Long> gameScores,
		List<GameQuestionGetResponse> questions) {

		KafkaMessage message = GameMessage.builder()
			.type(INFO)
			.submittedWord(submittedWord)
			.submitMemberId(accountId)
			.questions(questions)
			.gameScore(Map.copyOf(gameScores))
			.build()
			.convertToKafkaMessage("/from/game/%d".formatted(roomId));
		producer.produceMessage(message);
	}

	public void sendFinishGameMessage(Long roomId, List<InGameMemberGetResponse> allMembers,
		Map<Long, Long> totalScores) {

		KafkaMessage message = GameMessage.builder()
			.type(GameMessageType.FINISH)
			.allMembers(allMembers)
			.gameScore(Map.copyOf(totalScores))
			.build()
			.convertToKafkaMessage("/from/game/%d".formatted(roomId));
		producer.produceMessage(message);
	}
}
