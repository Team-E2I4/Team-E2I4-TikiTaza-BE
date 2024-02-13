package com.pgms.apisocket.dto.response;

import java.util.List;
import java.util.Map;

public record GameRoomStatusResponse(
	boolean isGameFinished, // true 되면 프론트는 overallScores 가지고 결과화면으로 ㅂㅂ
	boolean isRoundFinished, // ture 되면 프론트는 다음 라운드 준비
	Long currentRound, // 현재 라운드
	// 라운드 당 개인 점수 => 한타한타 라운드 진행중에 실시간으로 자동차 위치 업데이트를 위해서 쓰임
	List<Map<String, Long>> roundScores,
	// 총 합 개인 점수 => 라운드 끝났을 때 누적했다가 마지막에 랭킹과 함께 전달 => 우리가 GameRecode로 관리하므로 랭킹을 매겨서 전달
	List<Map<String, Long>> overallScores
) {
}
