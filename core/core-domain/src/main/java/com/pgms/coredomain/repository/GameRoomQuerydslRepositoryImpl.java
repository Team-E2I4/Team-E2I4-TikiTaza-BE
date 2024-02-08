package com.pgms.coredomain.repository;

import static com.pgms.coredomain.domain.game.QGameRoom.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.pgms.coredomain.domain.game.GameRoom;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GameRoomQuerydslRepositoryImpl implements GameRoomQuerydslRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GameRoom> findAll(Pageable pageable) {
		final List<GameRoom> gameRooms = queryFactory.selectFrom(gameRoom)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(gameRoom.count())
			.from(gameRoom);
		
		return PageableExecutionUtils.getPage(gameRooms, pageable, countQuery::fetchOne);
	}
}
