package com.pgms.api.domain.online.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "실시간 접속자 응답", requiredProperties = {"memberId", "nickname"})
public record OnlineMemberGetResponse(
	Long memberId,
	String nickname
) {
	public static OnlineMemberGetResponse of(Long memberId, String nickname) {
		return new OnlineMemberGetResponse(memberId, nickname);
	}
}
