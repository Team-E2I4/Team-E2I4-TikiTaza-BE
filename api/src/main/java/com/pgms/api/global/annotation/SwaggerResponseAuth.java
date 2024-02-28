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
	@ApiResponse(responseCode = "200", description = "성공"),
	@ApiResponse(responseCode = "400", description = """
		[sec-400/01] 유효하지 않은 토큰입니다. \t\n
		[sec-400/02] 유효하지 않은 소셜 로그인 코드입니다.""",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "401", description = """
		[sec-401/01] 로그인 해주세요. \t\n
		[sec-401/02] 토큰이 만료되었습니다. \t\n
		[sec-401/03] 다시 로그인 해주세요. \t\n
		[sec-401/04] 로그아웃 상태로 재로그인이 필요합니다.""",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "403", description = "[sec-403/01] 권한이 없습니다.",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "500", description = "[sec-500] 소셜 로그인 중 오류가 발생했습니다. 관리자에게 문의하세요.",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface SwaggerResponseAuth {
}
