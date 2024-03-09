package com.pgms.api.domain.game.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.response.GameQuestionGetResponse;
import com.pgms.api.domain.game.dto.response.InGameMemberGetResponse;
import com.pgms.api.global.exception.GameException;
import com.pgms.api.global.exception.SocketException;
import com.pgms.api.socket.dto.request.GameFinishRequest;
import com.pgms.api.socket.dto.request.GameInfoUpdateRequest;
import com.pgms.api.socket.dto.request.WordGameInfoUpdateRequest;
import com.pgms.api.socket.service.GameMessageService;
import com.pgms.api.sse.SseEmitters;
import com.pgms.api.sse.service.SseService;
import com.pgms.coredomain.domain.game.GameHistory;
import com.pgms.coredomain.domain.game.GameInfo;
import com.pgms.coredomain.domain.game.GameQuestion;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.domain.game.GameRoomMember;
import com.pgms.coredomain.domain.game.GameType;
import com.pgms.coredomain.exception.GameRoomErrorCode;
import com.pgms.coredomain.repository.GameHistoryRepository;
import com.pgms.coredomain.repository.GameInfoRepository;
import com.pgms.coredomain.repository.GameQuestionRepository;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coredomain.repository.GameRoomRepository;
import com.pgms.coreinfraredis.repository.RedisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

	private final GameMessageService gameMessageService;
	private final GameRoomRepository gameRoomRepository;
	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final GameQuestionRepository gameQuestionRepository;
	private final GameInfoRepository gameInfoRepository;
	private final GameHistoryRepository gameHistoryRepository;
	private final RedisRepository redisRepository;
	private final SseEmitters sseEmitters;
	private final SseService sseService;

	// ============================== 입장 확인 및 첫 라운드 스타트 ==============================
	public void startFirstRound(Long roomId, Long accountId, String sessionId) {
		final GameInfo gameInfo = getGameInfo(roomId);
		final GameRoom gameRoom = getGameRoom(roomId);
		final GameRoomMember gameRoomMember = gameRoomMemberRepository.findByMemberId(accountId)
			.orElseThrow(() -> new GameException(GameRoomErrorCode.GAME_ROOM_MEMBER_NOT_FOUND));

		// 입장 카운트 증가 및 유저 세션아이디 업데이트
		// TODO : 나중에 한명이 여러번 입장처리 못하게 수정
		gameInfo.enter();
		gameRoomMember.updateSessionId(sessionId);

		// 모든 멤버가 입장했을 때
		if (gameInfo.isAllEntered(gameRoom.getCurrentPlayer())) {
			// 문제와 유저 정보 전송 및 라운드 점수 초기화
			List<InGameMemberGetResponse> gameRoomMembers = sendQuestionsAndUserInfo(gameRoom, roomId);
			initRoundScores(roomId, gameRoomMembers);
		}
	}

	// ============================== 게임 중 실시간 업데이트 통신 (문장, 코딩) ==============================
	public void updateGameInfo(Long accountId, Long roomId, GameInfoUpdateRequest gameInfoUpdateRequest) {
		redisRepository.increaseRoundScore(String.valueOf(roomId), String.valueOf(accountId),
			gameInfoUpdateRequest.currentScore());
		// TODO: gameRoomRepository를 쓰는게 낫나?
		final List<GameRoomMember> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId);
		sendGameInfoMessage(roomId, gameRoomMembers);
	}

	// ============================== 게임 중 실시간 업데이트 통신 (짧은 단어) ==============================
	public void updateWordGameInfo(Long accountId, Long roomId, WordGameInfoUpdateRequest gameInfoUpdateRequest) {
		// 단어찾아와서 점수받기
		boolean isValidWord = redisRepository.updateWords(roomId.toString(), gameInfoUpdateRequest.word());
		if (isValidWord) {
			// 멤버 점수 올려주고, 반환
			redisRepository.increaseRoundWordScore(String.valueOf(roomId), String.valueOf(accountId));
			final List<GameRoomMember> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId);
			sendWordGameInfoMessage(roomId, accountId, gameInfoUpdateRequest.word(), gameRoomMembers);
		}
	}

	// ============================== 게임 종료 / 다음 라운드 시작 ==============================
	public void finishGame(Long roomId, GameFinishRequest gameFinishRequest) {
		final GameInfo gameInfo = getGameInfo(roomId);
		final GameRoom gameRoom = getGameRoom(roomId);
		final List<GameRoomMember> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId);

		gameInfo.submit();

		// 게임방에 있는 모두가 게임 종료를 누르면 게임 종료 처리
		if (gameInfo.isAllSubmitted(gameRoom.getCurrentPlayer())) {
			// 마지막 라운드면 /from/game/{roomId}/round-finish 로 전송 -> 라운드 점수를 최종 점수에 누적
			accumulateRoundScoresToTotalScores(roomId);
			if (gameFinishRequest.currentRound() == gameRoom.getRound()) {
				gameInfoRepository.delete(gameInfo);
				handleFinishGame(roomId, gameRoom, gameRoomMembers);
			} else {
				gameInfo.initSubmittedCount();
				startNextRound(roomId, gameRoom, gameRoomMembers);
			}
		}
	}

	private List<InGameMemberGetResponse> sendQuestionsAndUserInfo(GameRoom gameRoom, Long roomId) {
		final List<InGameMemberGetResponse> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId)
			.stream()
			.map(InGameMemberGetResponse::from)
			.toList();

		final List<GameQuestion> questions = getGameQuestions(gameRoom);
		final List<GameQuestionGetResponse> questionResponses = questions.stream()
			.map(question -> GameQuestionGetResponse.of(question.getQuestion()))
			.toList();

		log.info(">>>>>> find random Question !!!! {}", questions);

		// 단어 게임이면 레디스에 단어 초기화
		initWordsIfWordGame(gameRoom, roomId, questions);

		gameMessageService.sendQuestionsAndUserInfoMessage(roomId, questionResponses, gameRoomMembers);
		return gameRoomMembers;
	}

	private void initRoundScores(Long roomId, List<InGameMemberGetResponse> gameRoomMembers) {
		List<Long> memberIds = gameRoomMembers.stream()
			.map(InGameMemberGetResponse::memberId)
			.toList();

		// 라운드별 점수 초기화
		redisRepository.initRoundScores(String.valueOf(roomId), memberIds);
	}

	private List<GameQuestionGetResponse> getRemainWords(Long roomId) {
		List<String> remainWords = redisRepository.getWords(roomId.toString());
		return remainWords.stream()
			.map(GameQuestionGetResponse::of)
			.toList();
	}

	private void accumulateRoundScoresToTotalScores(Long roomId) {
		Map<Long, Long> roundScores = redisRepository.getRoundScores(String.valueOf(roomId));
		roundScores.forEach((memberId, score) ->
			redisRepository.increaseTotalScore(String.valueOf(roomId), String.valueOf(memberId), score));
	}

	private void updateGameRoomMemberScores(Map<Long, Long> totalScores, GameRoom gameRoom) {
		totalScores.forEach((memberId, score) -> {
			final GameHistory gameHistory = GameHistory.builder()
				.score(score.intValue())
				.gameType(gameRoom.getGameType())
				.memberId(memberId)
				.build();
			gameHistoryRepository.save(gameHistory);
		});
	}

	private void startNextRound(Long roomId, GameRoom gameRoom, List<GameRoomMember> gameRoomMembers) {
		final List<GameQuestion> questions = getGameQuestions(gameRoom);
		final List<GameQuestionGetResponse> questionResponses = questions.stream()
			.map(gameQuestion -> GameQuestionGetResponse.of(gameQuestion.getQuestion()))
			.toList();

		initWordsIfWordGame(gameRoom, roomId, questions);

		final List<InGameMemberGetResponse> allMembers = gameRoomMembers.stream()
			.map(InGameMemberGetResponse::from)
			.toList();

		gameMessageService.sendNextRoundMessage(roomId, allMembers, questionResponses);
		initRoundScores(roomId, allMembers);
	}

	private void handleFinishGame(Long roomId, GameRoom gameRoom, List<GameRoomMember> gameRoomMembers) {
		// 최종 점수 조회
		final Map<Long, Long> totalScores = redisRepository.getTotalScores(String.valueOf(roomId));
		gameRoom.updateGameRoomStatus(false);

		updateGameRoomMemberScores(totalScores, gameRoom);

		// 모든 유저들 게임 종료 후 준비 상태 해제
		updateReadyStatusAfterFinishGame(gameRoom, gameRoomMembers);
		final List<InGameMemberGetResponse> allMembers = getAllMembersWithTotalScore(roomId, gameRoomMembers);
		gameMessageService.sendFinishGameMessage(roomId, allMembers);

		// 게임 종료 시 sse 발행
		sseEmitters.updateGameRoom(sseService.getRooms());

		// 레디스에 저장된 점수 삭제
		redisRepository.deleteGameInfo(String.valueOf(roomId));
	}

	private void updateReadyStatusAfterFinishGame(GameRoom gameRoom, List<GameRoomMember> gameRoomMembers) {
		gameRoomMembers.forEach(gameRoomMember -> {
			if (!gameRoom.isHost(gameRoomMember.getMemberId())) {
				gameRoomMember.updateReadyStatus(false);
			}
		});
	}

	private void initWordsIfWordGame(GameRoom gameRoom, Long roomId, List<GameQuestion> questions) {
		if (gameRoom.getGameType() == GameType.WORD) {
			initWordsToRedis(roomId, questions);
		}
	}

	private void initWordsToRedis(Long roomId, List<GameQuestion> questions) {
		redisRepository.initWords(roomId.toString(),
			questions.stream().map(GameQuestion::getQuestion).toList());
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
		return gameQuestionRepository.findByGameTypeAndCount(gameType, PageRequest.of(0, gameType.getQuestionCount()));
	}

	private List<InGameMemberGetResponse> getAllMembersWithRoundScore(Long roomId,
		List<GameRoomMember> gameRoomMembers) {
		return gameRoomMembers.stream()
			.map(gameRoomMember -> {
				final Long memberId = gameRoomMember.getMemberId();
				final Long score = redisRepository.getRoundScore(String.valueOf(roomId), String.valueOf(memberId));
				return InGameMemberGetResponse.from(gameRoomMember, score);
			}).toList();
	}

	private List<InGameMemberGetResponse> getAllMembersWithTotalScore(Long roomId,
		List<GameRoomMember> gameRoomMembers) {
		return gameRoomMembers.stream()
			.map(gameRoomMember -> {
				final Long memberId = gameRoomMember.getMemberId();
				final Long score = redisRepository.getTotalScore(String.valueOf(roomId), String.valueOf(memberId));
				return InGameMemberGetResponse.from(gameRoomMember, score);
			}).toList();
	}

	private void sendGameInfoMessage(Long roomId, List<GameRoomMember> gameRoomMembers) {
		final List<InGameMemberGetResponse> gameInfo = getAllMembersWithRoundScore(roomId, gameRoomMembers);
		gameMessageService.sendGameInfoMessage(roomId, gameInfo);
	}

	private void sendWordGameInfoMessage(Long roomId, Long accountId, String submittedWord,
		List<GameRoomMember> gameRoomMembers) {
		// 남은 단어 목록 반환
		List<GameQuestionGetResponse> remainWords = getRemainWords(roomId);
		final List<InGameMemberGetResponse> gameInfo = getAllMembersWithRoundScore(roomId, gameRoomMembers);
		gameMessageService.sendWordGameInfoMessage(roomId, accountId, submittedWord, remainWords, gameInfo);
	}
}
