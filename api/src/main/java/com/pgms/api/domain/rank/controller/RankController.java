package com.pgms.api.domain.rank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.rank.service.RankingService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coreinfraredis.dto.RankingResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "랭킹", description = "랭킹 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
public class RankController {

	private final RankingService rankingService;

	@Operation(summary = "랭킹 조회")
	@GetMapping("/api/v1/ranks")
	public ResponseEntity<ApiResponse<List<RankingResponse>>> getRanking(
		@RequestParam(value = "gameType", required = false) String gameType) {
		List<RankingResponse> memberRanking = rankingService.getRanking(gameType);
		return ResponseEntity.ok(ApiResponse.of(memberRanking));
	}
}
