package com.pgms.apisocket.service;

import static com.pgms.coredomain.domain.common.GameRoomErrorCode.*;
import static com.pgms.coredomain.domain.common.MemberErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apisocket.dto.GameRoomEnterResponse;
import com.pgms.apisocket.dto.GameRoomExitResponse;
import com.pgms.apisocket.exception.SocketException;
import com.pgms.apisocket.handler.CustomWebSocketHandlerDecorator;
import com.pgms.coredomain.domain.common.GameErrorCode;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.repository.GameRoomRepository;
import com.pgms.coredomain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GameRoomSocketService {

	private final GameRoomRepository gameRoomRepository;
	private final MemberRepository memberRepository;
	private final CustomWebSocketHandlerDecorator customWebSocketHandlerDecorator;

	public GameRoomEnterResponse enterGameRoom(Long roomId, Long memberId) {
		final Member member = getMember(memberId);

		// 방이 존재하는가? -> 존재하지 않으면 입장 불가
		final GameRoom gameRoom = getGameRoom(roomId);

		// 진행중인 게임인가? -> 진행중이면 입장 불가
		if (gameRoom.isStarted()) {
			throw new SocketException(GameErrorCode.GAME_ALREADY_STARTED);
		}
		// 방에 멤버가 꽉 찼는가? -> 꽉 찼으면 입장 불가
		if (gameRoom.isFull()) {
			throw new SocketException(GAME_ROOM_FULL);
		}
		// 멤버가 방에 들어가있는 건 아닌가? -> roomId + memberId로 소켓을 찾아서 연결을 끊어버림
		if (member.getGameRoom() != null) {
			final String sessionId = member.getId() + ":" + member.getGameRoom().getId();
			customWebSocketHandlerDecorator.closeSession(sessionId);
		}
		// 방에 멤버 추가 -> 게임룸은 새로 들어가려는 방
		gameRoom.enterGameRoom(member);
		return GameRoomEnterResponse.from(member);
	}

	public GameRoomExitResponse exitGameRoom(Long roomId, Long memberId) {
		// 멤버가 존재하는가
		final Member member = getMember(memberId);
		// 방이 존재하는가
		final GameRoom gameRoom = getGameRoom(roomId);
		validateIsMatchedGameRoom(member, roomId);
		gameRoom.exitGameRoom(member);
		return GameRoomExitResponse.from(member);
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new SocketException(MEMBER_NOT_FOUND));
	}

	private GameRoom getGameRoom(Long roomId) {
		return gameRoomRepository.findById(roomId)
			.orElseThrow(() -> new SocketException(GAME_ROOM_NOT_FOUND));
	}

	private void validateIsMatchedGameRoom(Member member, Long roomId) {
		if (!member.getGameRoom().getId().equals(roomId)) {
			throw new SocketException(GAME_ROOM_MISMATCH);
		}
	}
}
