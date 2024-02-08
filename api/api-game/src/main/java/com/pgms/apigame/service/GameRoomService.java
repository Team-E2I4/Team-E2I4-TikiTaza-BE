package com.pgms.apigame.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apigame.dto.GameRoomCreateRequest;
import com.pgms.apigame.dto.GameRoomGetResponse;
import com.pgms.apigame.dto.PageCondition;
import com.pgms.apigame.dto.PageResponse;
import com.pgms.apigame.exception.GameException;
import com.pgms.coredomain.domain.common.GameErrorCode;
import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.repository.GameRoomRepository;
import com.pgms.coredomain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GameRoomService {

	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;

	public Long createRoom(Long memberId, GameRoomCreateRequest request) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new GameException(MemberErrorCode.MEMBER_NOT_FOUND));
		gameRoomRepository.findByOwnerId(memberId)
			.ifPresent(gameRoom -> {
				throw new GameException(GameErrorCode.ROOM_ALREADY_EXISTS);
			});

		return gameRoomRepository.save(request.toEntity(memberId)).getId();
	}

	@Transactional(readOnly = true)
	public PageResponse<GameRoomGetResponse> getRooms(PageCondition pageCondition) {
		final Page<GameRoomGetResponse> gameRooms = gameRoomRepository.findAll(pageCondition.getPageable())
			.map(GameRoomGetResponse::from);
		return PageResponse.from(gameRooms);
	}

}
