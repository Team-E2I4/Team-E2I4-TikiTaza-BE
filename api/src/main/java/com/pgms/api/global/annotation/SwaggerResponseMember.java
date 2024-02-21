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
		[mem-400/01] 입력값에 대한 검증에 실패했습니다.
		[mem-400/02] 탈퇴한 회원입니다.
		[mem-400/03] 비밀번호 확인이 일치하지 않습니다.""",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "404", description = "[mem-404/01] 회원을 찾을 수 없습니다.",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "409", description = """
		[mem-409/01] 이미 존재하는 이메일입니다.
		[mem-409/02] 이미 존재하는 닉네임입니다.""")
})
public @interface SwaggerResponseMember {
}
