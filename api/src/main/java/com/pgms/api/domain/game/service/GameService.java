package com.pgms.api.domain.game.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.response.GameInfoUpdateResponse;
import com.pgms.api.domain.game.dto.response.GameQuestionGetResponse;
import com.pgms.api.domain.game.dto.response.GameRoomMemberGetResponse;
import com.pgms.api.global.exception.SocketException;
import com.pgms.api.socket.dto.Message;
import com.pgms.api.socket.dto.MessageType;
import com.pgms.api.socket.dto.request.GameFinishRequest;
import com.pgms.api.socket.dto.request.GameInfoUpdateRequest;
import com.pgms.api.socket.dto.request.WordGameInfoUpdateRequest;
import com.pgms.api.sse.SseEmitters;
import com.pgms.api.sse.service.SseService;
import com.pgms.coredomain.domain.common.GameRoomErrorCode;
import com.pgms.coredomain.domain.game.GameInfo;
import com.pgms.coredomain.domain.game.GameQuestion;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.domain.game.GameType;
import com.pgms.coredomain.repository.GameInfoRepository;
import com.pgms.coredomain.repository.GameQuestionRepository;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coredomain.repository.GameRoomRepository;
import com.pgms.coreinfrakafka.kafka.KafkaMessage;
import com.pgms.coreinfrakafka.kafka.producer.Producer;
import com.pgms.coreinfraredis.repository.RedisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

	private final GameRoomRepository gameRoomRepository;
	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final GameQuestionRepository gameQuestionRepository;
	private final GameInfoRepository gameInfoRepository;
	private final RedisRepository redisRepository;
	private final SseEmitters sseEmitters;
	private final SseService sseService;
	private final Producer producer;

	// ============================== 게임 시작 ==============================
	public void startGame(Long roomId, Long memberId) {
		// 현재 방 정보 가져오기
		final GameRoom gameRoom = getGameRoom(roomId);

		// 시작버튼 누른 유저 검증 (있는 유저인지 & 방장인지)
		if (!gameRoom.getHostId().equals(memberId)) {
			throw new SocketException(roomId, GameRoomErrorCode.GAME_ROOM_HOST_MISMATCH);
		}

		// 방에 있는 인원들 준비상태 확인
		if (gameRoom.isAllReady()) {
			// 방 아이디로 실제 객체의 start 여부 변경 -> 더이상 입장 못함
			gameRoom.updateGameRoomStatus(true);

			// 게임 시작 메세지 뿌리기
			KafkaMessage message = Message.builder()
				.type(MessageType.START)
				.build()
				.convertToKafkaMessage("/from/game-room/%d".formatted(gameRoom.getId()));

			producer.produceMessage(message);
			// 방 정보 뿌리기 -> 게임 중인 방은 로비에서 보이면 안되니까
			sseEmitters.updateGameRoom(sseService.getRooms());
		} else {
			// 실패 메세지 뿌리기
			KafkaMessage message = Message.builder()
				.type(MessageType.START_DENIED)
				.build()
				.convertToKafkaMessage("/from/game-room/%d".formatted(gameRoom.getId()));
			producer.produceMessage(message);
		}
	}

	// ============================== 모두 입장한걸 확인하고 문제와 유저 정보 전송 ==============================
	public void firstRoundStart(Long roomId) {
		final GameInfo gameInfo = getGameInfo(roomId);
		final GameRoom gameRoom = getGameRoom(roomId);

		// 카운트만 올려줌
		gameInfo.enter();

		// 입장인원 == GameRoom.getMemberCount 진짜시작 / 제출인원 == GameRoom.getMemberCount 진짜 다음라운드
		// 전부 입장했다 -> 문제와 유저 정보 전송 (진짜 시작) /from/game/{roomId}/round-start
		if (gameInfo.isAllEntered(gameRoom.getCurrentPlayer())) {
			// 문제와 유저 정보 전송

			final List<GameRoomMemberGetResponse> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId)
				.stream()
				.map(GameRoomMemberGetResponse::from)
				.toList();

			final List<GameQuestion> questions = getGameQuestions(gameRoom);
			final List<GameQuestionGetResponse> questionResponses = questions.stream()
				.map(GameQuestionGetResponse::of).toList();

			if (gameRoom.getGameType() == GameType.WORD) {
				redisRepository.initWords(roomId.toString(),
					questions.stream().map(GameQuestion::getQuestion).toList());
			}

			KafkaMessage message = Message.builder()
				.type(MessageType.ROUND_START)
				.allMembers(gameRoomMembers)
				.questions(questionResponses)
				.build()
				.convertToKafkaMessage("/from/game/%d/round-start".formatted(gameRoom.getId()));
			producer.produceMessage(message);

			List<Long> memberIds = gameRoomMembers.stream()
				.map(GameRoomMemberGetResponse::memberId)
				.toList();

			// 게임방 정보 만료시간 설정
			redisRepository.initMemberScores(String.valueOf(roomId), memberIds);
		}
	}

	// ============================== 게임 중 실시간 업데이트 통신 (문장, 코딩) ==============================
	public void updateGameInfo(Long memberId, Long roomId,
		GameInfoUpdateRequest gameInfoUpdateRequest) {
		redisRepository.updateRoundMemberScore(
			String.valueOf(roomId),
			String.valueOf(memberId),
			gameInfoUpdateRequest.currentScore());

		final Map<Long, Long> sortedScores = redisRepository.getRoundScores(String.valueOf(roomId));

		GameInfoUpdateResponse gameRoomInfoUpdateResponse = GameInfoUpdateResponse.from(sortedScores);

		KafkaMessage message = Message.builder()
			.type(MessageType.UPDATE)
			.gameScore(gameRoomInfoUpdateResponse)
			.build()
			.convertToKafkaMessage("/from/game/%d/info".formatted(roomId));
		producer.produceMessage(message);
	}

	// ============================== 게임 중 실시간 업데이트 통신 (짧은 단어) ==============================
	public void updateWordGameInfo(Long memberId, Long roomId,
		WordGameInfoUpdateRequest gameInfoUpdateRequest) {

		// 단어찾아와서 점수받기
		Long score = redisRepository.updateWords(roomId.toString(), gameInfoUpdateRequest.word());
		// 점수가 0이아니면 점수 업데이트 + 단어 정보 반환
		if (score != 0) {
			// 멤버 점수 올려주고, 반환
			redisRepository.updateRoundMemberScore(
				String.valueOf(roomId),
				String.valueOf(memberId),
				score);

			final Map<Long, Long> sortedScores = redisRepository.getRoundScores(String.valueOf(roomId));

			GameInfoUpdateResponse gameRoomInfoUpdateResponse = GameInfoUpdateResponse.from(sortedScores);

			KafkaMessage message = Message.builder()
				.type(MessageType.UPDATE)
				.gameScore(gameRoomInfoUpdateResponse)
				.submittedWord(gameInfoUpdateRequest.word())
				.build()
				.convertToKafkaMessage("/from/game/%d/info".formatted(roomId));
			producer.produceMessage(message);
		} else {
			// 점수가 0이면 안된다 응답
			KafkaMessage message = Message.builder()
				.type(MessageType.WORD_DENIED)
				.build()
				.convertToKafkaMessage("/from/game/%d/info".formatted(roomId));
			producer.produceMessage(message);
		}
	}

	// ============================== 게임 종료 & 다음 라운드 시작 ==============================
	public void finishGame(Long roomId, GameFinishRequest gameFinishRequest) {
		final GameInfo gameInfo = getGameInfo(roomId);
		final GameRoom gameRoom = getGameRoom(roomId);
		final List<GameRoomMemberGetResponse> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId)
			.stream()
			.map(GameRoomMemberGetResponse::from)
			.toList();

		gameInfo.submit();

		// 게임방에 있는 모두가 게임 종료를 누르면 게임 종료 처리
		if (gameInfo.isAllSubmitted(gameRoom.getCurrentPlayer())) {
			// 마지막 라운드면 /from/game/{roomId}/round-finish 로 전송
			// 라운드 점수를 최종 점수에 누적
			final Map<Long, Long> roundScores = redisRepository.getRoundScores(String.valueOf(roomId));

			roundScores.forEach((key, value) -> redisRepository.updateTotalMemberScore(
				String.valueOf(roomId),
				String.valueOf(key),
				value));

			if (gameFinishRequest.currentRound() == gameRoom.getRound()) {
				// 최종 점수 조회
				final Map<Long, Long> totalScores = redisRepository.getTotalScores(String.valueOf(roomId));
				GameInfoUpdateResponse gameRoomInfoUpdateResponse = GameInfoUpdateResponse.from(totalScores);

				KafkaMessage message = Message.builder()
					.type(MessageType.FINISH)
					.allMembers(gameRoomMembers)
					.gameScore(gameRoomInfoUpdateResponse)
					.build()
					.convertToKafkaMessage("/from/game/%d/finish".formatted(roomId));
				producer.produceMessage(message);
			} else {
				List<Long> memberIds = gameRoomMembers.stream()
					.map(GameRoomMemberGetResponse::memberId)
					.toList();

				final List<GameQuestion> questions = getGameQuestions(gameRoom);
				final List<GameQuestionGetResponse> questionResponses = questions.stream()
					.map(GameQuestionGetResponse::of).toList();

				if (gameRoom.getGameType() == GameType.WORD) {
					redisRepository.initWords(roomId.toString(),
						questions.stream().map(GameQuestion::getQuestion).toList());
				}

				KafkaMessage message = Message.builder()
					.type(MessageType.ROUND_START)
					.allMembers(gameRoomMembers)
					.questions(questionResponses)
					.build()
					.convertToKafkaMessage("/from/game/%d/round-start".formatted(gameRoom.getId()));
				producer.produceMessage(message);

				// 라운드별 점수 초기화
				redisRepository.initMemberScores(String.valueOf(roomId), memberIds);
			}
		}
	}

	private GameRoom getGameRoom(Long roomId) {
		return gameRoomRepository.findById(roomId)
			.orElseThrow(() -> new SocketException(roomId, GameRoomErrorCode.GAME_ROOM_NOT_FOUND));
	}

	private GameInfo getGameInfo(Long roomId) {
		return gameInfoRepository.findByGameRoomId(roomId)
			.orElseThrow(() -> new SocketException(roomId, GameRoomErrorCode.GAME_INFO_NOT_FOUND));
	}

	private List<GameQuestion> getGameQuestions(GameRoom gameRoom) {
		final GameType gameType = gameRoom.getGameType();
		return gameQuestionRepository.findByGameTypeAndCount(gameType,
			PageRequest.of(0, gameType.getQuestionCount()));

	}
}
