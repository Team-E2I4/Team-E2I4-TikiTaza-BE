package com.pgms.api.socket.dto.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pgms.api.domain.game.dto.response.GameQuestionGetResponse;
import com.pgms.api.domain.game.dto.response.InGameMemberGetResponse;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameMessage extends Message {
	private GameMessageType type;                             // 게임 메세지 타입
	protected List<InGameMemberGetResponse> allMembers;       // 전체 유저 정보
	private String submittedWord;                             // 제출된 단어
	private Long submitMemberId;                              // 제출한 회원 ID
	private Map<Long, Long> gameScore;                        // 게임 점수
	private List<GameQuestionGetResponse> questions;          // 게임 질문 리스트
}
