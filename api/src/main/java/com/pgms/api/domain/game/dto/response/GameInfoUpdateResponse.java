package com.pgms.api.domain.game.dto.response;

import java.util.Map;

public record GameInfoUpdateResponse(
	// 단어는 우리가 입력할수 있는 단어 셋을 다 들고있다가 해당 단어에 대해서 특정 플레이가 입력 요청이 오면 선점됐는지를 판단후에 점수를 올려준다 or 실패했다고할린다. GameRoomId:단어 50세트
	// <memberId, score>
	Map<Long, Long> gameScore
) {
	public static GameInfoUpdateResponse from(Map<Long, Long> sortedScores) {
		return new GameInfoUpdateResponse(sortedScores);
	}
}
