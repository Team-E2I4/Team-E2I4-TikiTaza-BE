package com.pgms.apigame.dto;

import com.pgms.coredomain.domain.game.GameRoom;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GameRoomCreateRequest(

	@NotBlank
	@Size(min = 1, max = 20, message = "[ERROR] 타이틀은 1자 이상 20자 이하로 입력해주세요.")
	String title,

	@Size(min = 1, max = 20, message = "[ERROR] 비밀번호는 1자 이상 20자 이하로 입력해주세요.")
	String password,

	@NotNull
	@Min(value = 2, message = "[ERROR] 최대 인원은 2명 이상으로 입력해주세요.")
	@Max(value = 8, message = "[ERROR] 최대 인원은 8명 이하로 입력해주세요.")
	Integer maxPlayer,

	@NotNull
	@Min(value = 1, message = "[ERROR] 라운드 횟수는 1회 이상으로 입력해주세요.")
	@Max(value = 10, message = "[ERROR] 라운드 횟수는 10회 이하로 입력해주세요.")
	Integer roundCount
) {
	public GameRoom toEntity(Long ownerId) {
		return GameRoom.builder()
			.title(title)
			.password(password)
			.maxPlayer(maxPlayer)
			.roundCount(roundCount)
			.ownerId(ownerId)
			.build();
	}
}
