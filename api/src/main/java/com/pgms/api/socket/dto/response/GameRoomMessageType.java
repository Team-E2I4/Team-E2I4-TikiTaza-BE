package com.pgms.api.socket.dto.response;

public enum GameRoomMessageType {
	ENTER,              // 플레이어 입장
	EXIT,               // 플레이어 퇴장
	READY,              // 플레이어 준비 상태 변경
	START,              // 게임 시작 (방장)
	START_DENIED,       // 게임 시작 거부 (방장이 아닌 경우)
	KICKED,             // 플레이어 강퇴 (방장)
	MODIFIED,           // 게임방 정보 수정 (방장)
}
