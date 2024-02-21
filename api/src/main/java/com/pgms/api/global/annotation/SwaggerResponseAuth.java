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
	@ApiResponse(responseCode = "401", description = """
		[sec-401/01] 로그인이 필요합니다.
		[sec-401/02] 액세스 토큰이 만료되었으니 다시 로그인 해주세요.
		[sec-401/03] 리프레시 토큰이 만료되었으니 다시 로그인 해주세요.
		[sec-401/04] 현재 토큰은 로그아웃 상태로 재로그인이 필요합니다.""",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	@ApiResponse(responseCode = "403", description = "[sec-403/01] 해당 토큰에 접근 권한이 없습니다.",
		content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface SwaggerResponseAuth {
}
