package com.pgms.api.socket.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pgms.api.domain.game.dto.response.GameQuestionGetResponse;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameMessage extends Message {
	private GameMessageType type;                             // 게임 메세지 타입
	private String submittedWord;                             // 사용된 단어
	private Map<Long, Long> gameScore;                 // 게임 점수
	private List<GameQuestionGetResponse> questions;          // 게임 질문 리스트
}
