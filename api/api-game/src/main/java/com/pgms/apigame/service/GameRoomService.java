package com.pgms.apigame.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		Pageable pageable = PageRequest.of(pageCondition.getPage() - 1, pageCondition.getSize());
		final Page<GameRoomGetResponse> gameRooms = gameRoomRepository.findAll(pageable)
			.map(GameRoomGetResponse::from);
		return PageResponse.from(gameRooms);
	}
}
