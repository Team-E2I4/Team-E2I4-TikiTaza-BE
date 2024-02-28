package com.pgms.api.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pgms.coredomain.response.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation()
@ApiResponses(value = {
	@ApiResponse(responseCode = "200", description = "정상 처리되었습니다."),
	@ApiResponse(responseCode = "201", description = "정상적으로 생성되었습니다."),
	@ApiResponse(responseCode = "400", description = """
		[gr-400/01] 입력값에 대한 검증에 실패했습니다.\t\n
		[gr-400/02] 방이 꽉 찼습니다.\t\n
		[gr-400/03] 방이 일치하지 않습니다.\t\n
		[gr-400/04] 방 비밀번호가 일치하지 않습니다.\t\n
		[gr-400/05] 유효하지 않은 게임 타입입니다.\t\n
		[gr-400/06] 초대코드가 유효하지 않습니다.\t\n""",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "404", description = """
		[mem-404/01] 회원을 찾을 수 없습니다.\t\n
		[gr-404/01] 게임방을 찾을 수 없습니다.\t\n
		[gr-404/02] 게임방 멤버를 찾을 수 없습니다.\t\n
		[gr-404/03] 게임 정보를 찾을 수 없습니다.""",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "409", description = """
		[gr-409/01] 이미 해당 유저의 방이 존재합니다.\t\n
		[gr-409/02] 방장이 아닙니다.
		[gr-409/03] 방 최대 인원이 현재 인원보다 작습니다.\t\n
		[gr-409/04] 같은 방에 이미 참여하고 있습니다.\t\n""",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface SwaggerResponseGameRoom {
}
