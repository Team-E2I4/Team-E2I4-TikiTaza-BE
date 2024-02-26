package com.pgms.api.domain.rank.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.api.domain.rank.dto.MemberRankResponse;
import com.pgms.coredomain.domain.game.GameRank;
import com.pgms.coredomain.domain.game.GameType;
import com.pgms.coredomain.repository.GameRankRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankService {

	private final GameRankRepository gameRankRepository;

	public List<MemberRankResponse> getMemberRanking(String gameType) {
		List<GameRank> gameRanks = gameType == null ?
			gameRankRepository.findAllRanking(null) :
			gameRankRepository.findAllRanking(GameType.of(gameType));

		return gameRanks.stream()
			.map(MemberRankResponse::from)
			.toList();
	}
}
