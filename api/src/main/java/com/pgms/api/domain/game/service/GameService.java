package com.pgms.api.domain.game.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.response.GameQuestionGetResponse;
import com.pgms.api.domain.game.dto.response.InGameMemberGetResponse;
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
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.exception.GameRoomErrorCode;
import com.pgms.coredomain.repository.GameHistoryRepository;
import com.pgms.coredomain.repository.GameInfoRepository;
import com.pgms.coredomain.repository.GameQuestionRepository;
import com.pgms.coredomain.repository.GameRoomMemberRepository;
import com.pgms.coredomain.repository.GameRoomRepository;
import com.pgms.coredomain.repository.MemberRepository;
import com.pgms.coreinfraredis.repository.RedisInGameRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

	private final GameMessageService gameMessageService;
	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;
	private final GameRoomMemberRepository gameRoomMemberRepository;
	private final GameQuestionRepository gameQuestionRepository;
	private final GameInfoRepository gameInfoRepository;
	private final GameHistoryRepository gameHistoryRepository;
	private final RedisInGameRepository redisInGameRepository;
	private final SseEmitters sseEmitters;
	private final SseService sseService;

	// ============================== 입장 확인 및 첫 라운드 스타트 ==============================
	public void startFirstRound(Long roomId) {
		final GameInfo gameInfo = getGameInfo(roomId);
		final GameRoom gameRoom = getGameRoom(roomId);

		gameInfo.enter();

		// 모든 멤버가 입장했을 때
		if (gameInfo.isAllEntered(gameRoom.getCurrentPlayer())) {
			// 문제와 유저 정보 전송 및 라운드 점수 초기화
			List<InGameMemberGetResponse> gameRoomMembers = sendQuestionsAndUserInfo(gameRoom, roomId);
			initRoundScores(roomId, gameRoomMembers);
		}
	}

	// ============================== 게임 중 실시간 업데이트 통신 (문장, 코딩) ==============================
	public void updateGameInfo(Long accountId, Long roomId, GameInfoUpdateRequest gameInfoUpdateRequest) {
		redisInGameRepository.increaseRoundScore(String.valueOf(roomId), String.valueOf(accountId),
			gameInfoUpdateRequest.currentScore());

		final List<GameRoomMember> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId);
		sendGameInfoMessage(roomId, gameRoomMembers);
	}

	// ============================== 게임 중 실시간 업데이트 통신 (짧은 단어) ==============================
	public void updateWordGameInfo(Long accountId, Long roomId, WordGameInfoUpdateRequest gameInfoUpdateRequest) {
		// 단어찾아와서 점수받기
		boolean isValidWord = redisInGameRepository.updateWords(roomId.toString(), gameInfoUpdateRequest.word());
		if (isValidWord) {
			// 멤버 점수 올려주고, 반환
			redisInGameRepository.increaseRoundWordScore(String.valueOf(roomId), String.valueOf(accountId));
			final List<GameRoomMember> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId);
			sendWordGameInfoMessage(roomId, accountId, gameInfoUpdateRequest.word(), gameRoomMembers);
		}
	}

	// ============================== 게임 종료 / 다음 라운드 시작 ==============================
	public void finishGame(Long accountId, Long roomId, GameFinishRequest gameFinishRequest) {
		final GameInfo gameInfo = getGameInfo(roomId);
		final GameRoom gameRoom = getGameRoom(roomId);
		final List<GameRoomMember> gameRoomMembers = gameRoomMemberRepository.findAllByGameRoomId(roomId);

		gameInfo.submit();

		if (!gameRoom.getGameType().equals(GameType.WORD)) {
			double weight = calculateGameWeight(gameInfo, gameRoom.getCurrentPlayer());
			redisInGameRepository.increaseWeight(String.valueOf(roomId), String.valueOf(accountId), weight);
		}

		// 게임방에 있는 모두가 게임 종료를 누르면 게임 종료 처리
		if (gameInfo.isAllSubmitted(gameRoom.getCurrentPlayer())) {
			updateMemberStats(gameRoomMembers, gameFinishRequest);
			accumulateRoundScoresToTotalScores(roomId);
			if (gameFinishRequest.currentRound() >= gameRoom.getRound()) {
				gameInfoRepository.delete(gameInfo);
				handleFinishGame(roomId, gameRoom, gameRoomMembers);
			} else {
				gameInfo.initSubmittedCount();
				startNextRound(roomId, gameRoom, gameRoomMembers);
			}
		}
	}

	private double calculateGameWeight(GameInfo gameInfo, int totalPlayerCount) {
		// 게임 시작 시각과 현재 시각(제출 시각)을 구함
		LocalDateTime gameStartAt = gameInfo.getCreatedAt();
		LocalDateTime currentTime = LocalDateTime.now();

		// 순위 가중치 계산: 전체 플레이어 수에서 실제 제출한 멤버 수를 뺌
		int rankWeight = totalPlayerCount - gameInfo.getSubmittedMemberCount();

		// 현재 시각과 게임 시작 시각 사이의 밀리초 차이 계산
		long timeDifferenceInMillis = ChronoUnit.MILLIS.between(gameStartAt, currentTime);

		// 밀리초 당 가중치 증가율
		double increaseRatePerMillisecond = 0.000001;

		// 최종 가중치 계산: 순위 가중치에 시간 차이에 따른 가중치 증가분을 더함
		return rankWeight + (timeDifferenceInMillis * increaseRatePerMillisecond);
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

		log.info(">>>>>> 랜덤으로 문제를 가져옵니다! : {}", questions);

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
		redisInGameRepository.initRoundScores(String.valueOf(roomId), memberIds);
	}

	private List<GameQuestionGetResponse> getRemainWords(Long roomId) {
		List<String> remainWords = redisInGameRepository.getWords(roomId.toString());
		return remainWords.stream()
			.map(GameQuestionGetResponse::of)
			.toList();
	}

	private void accumulateRoundScoresToTotalScores(Long roomId) {
		Map<Long, Double> roundScores = redisInGameRepository.getRoundScores(String.valueOf(roomId));

		roundScores.forEach((accountId, score) ->
			redisInGameRepository.increaseTotalScore(String.valueOf(roomId), String.valueOf(accountId), score));
	}

	private void saveGameHistory(Map<Long, Double> totalScores, GameRoom gameRoom) {
		totalScores.forEach((memberId, score) -> {
			final GameHistory gameHistory = GameHistory.builder()
				.score(score.longValue())
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
		final Map<Long, Double> totalScores = redisInGameRepository.getTotalScores(String.valueOf(roomId));

		gameRoomMembers.forEach(gameRoomMember -> memberRepository.findById(gameRoomMember.getMemberId())
			.ifPresent(Member::increaseGameCount));

		// 게임 카운트 증가 및 히스토리 저장
		saveGameHistory(totalScores, gameRoom);

		// 모든 유저들 게임 종료 후 준비 상태 해제
		gameRoom.updateGameRoomStatus(false);
		updateReadyStatusAfterFinishGame(gameRoom, gameRoomMembers);
		final List<InGameMemberGetResponse> allMembers = getAllMembersWithTotalScore(roomId, gameRoomMembers);
		gameMessageService.sendFinishGameMessage(roomId, allMembers);

		// 게임 종료 시 sse 발행
		sseEmitters.updateGameRoom(sseService.getRooms());

		// 레디스에 저장된 점수 삭제
		redisInGameRepository.deleteGameInfo(String.valueOf(roomId));
	}

	private void updateMemberStats(List<GameRoomMember> gameRoomMembers, GameFinishRequest gameFinishRequest) {
		gameRoomMembers.forEach(gameRoomMember -> memberRepository.findById(gameRoomMember.getMemberId())
			.ifPresent(member -> member.updateMemberStats(gameFinishRequest.cpm(), gameFinishRequest.accuracy())));
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
		redisInGameRepository.initWords(roomId.toString(),
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
				final double score = redisInGameRepository.getRoundScore(String.valueOf(roomId),
					String.valueOf(memberId));
				return InGameMemberGetResponse.from(gameRoomMember, score);
			}).toList();
	}

	private List<InGameMemberGetResponse> getAllMembersWithTotalScore(Long roomId,
		List<GameRoomMember> gameRoomMembers) {
		return gameRoomMembers.stream()
			.map(gameRoomMember -> {
				final Long memberId = gameRoomMember.getMemberId();
				final Double score = redisInGameRepository.getTotalScore(String.valueOf(roomId),
					String.valueOf(memberId));
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
