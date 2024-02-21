package com.pgms.api.socket.dto;

public enum MessageType {
	ENTER,              // 입장
	EXIT,               // 퇴장
	READY,              // 준비 완료
	START,              // 게임 시작
	START_DENIED,       // 게임 시작 거부
	FINISH,             // 게임 종료
	ROUND_START,        // 라운드 시작
	KICKED,             // 강퇴 당한 경우
	UPDATE,             // 게임 정보(점수) 업데이트
	WORD_DENIED         // 단어 거부 (이미 사용한 단어)
}
