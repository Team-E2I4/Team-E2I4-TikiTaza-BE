package com.pgms.api.sse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.game.dto.response.GameRoomGetResponse;
import com.pgms.coredomain.domain.game.GameRoom;
import com.pgms.coredomain.repository.GameRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

	private final GameRoomRepository gameRoomRepository;

	@Transactional(readOnly = true)
	public List<GameRoomGetResponse> getRooms() {
		List<GameRoom> gameRooms = gameRoomRepository.findAll();

		return gameRooms.stream()
			.map(GameRoomGetResponse::from)
			.toList();
	}
}
