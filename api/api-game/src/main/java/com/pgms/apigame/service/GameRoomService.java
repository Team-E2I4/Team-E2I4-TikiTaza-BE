package com.pgms.apigame.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.pgms.apigame.dto.GameRoomGetResponse;
import com.pgms.apigame.dto.PageCondition;
import com.pgms.apigame.dto.PageResponse;
import com.pgms.coredomain.repository.GameRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameRoomService {

	private final GameRoomRepository gameRoomRepository;

	public PageResponse<GameRoomGetResponse> getRooms(PageCondition pageCondition) {
		final Page<GameRoomGetResponse> gameRooms = gameRoomRepository.findAll(pageCondition.getPageable())
			.map(GameRoomGetResponse::from);
		return PageResponse.from(gameRooms);
	}
}
