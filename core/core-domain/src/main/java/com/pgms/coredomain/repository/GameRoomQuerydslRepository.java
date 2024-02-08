package com.pgms.coredomain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pgms.coredomain.domain.game.GameRoom;

public interface GameRoomQuerydslRepository {

	Page<GameRoom> findAll(Pageable pageable);
}
