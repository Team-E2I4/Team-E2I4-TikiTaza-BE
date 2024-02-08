package com.pgms.coredomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.game.GameRoom;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long>, GameRoomQuerydslRepository {

}
