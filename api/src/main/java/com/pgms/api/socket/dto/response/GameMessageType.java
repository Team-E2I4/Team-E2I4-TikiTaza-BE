package com.pgms.api.socket.dto.response;

public enum GameMessageType {
	// 게임 메세지 타입
	FIRST_ROUND_START,  // 첫 라운드 시작
	NEXT_ROUND_START,   // 다음 라운드 시작
	INFO,               // 게임 점수
	FINISH              // 게임 종료
}
