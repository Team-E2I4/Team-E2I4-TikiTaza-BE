package com.pgms.api.domain.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation()
@ApiResponses({
	@ApiResponse(responseCode = "200", description = "성공"),
	@ApiResponse(responseCode = "sec-401/01", description = "로그인이 필요합니다.", content = @Content),
	@ApiResponse(responseCode = "sec-401/02", description = "액세스 토큰이 만료되었으니 다시 로그인 해주세요.", content = @Content),
	@ApiResponse(responseCode = "sec-401/03", description = "리프레시 토큰이 만료되었으니 다시 로그인 해주세요.", content = @Content),
	@ApiResponse(responseCode = "sec-401/04", description = "현재 토큰은 로그아웃 상태로 재로그인이 필요합니다.", content = @Content),
	@ApiResponse(responseCode = "sec-403/01", description = "해당 토큰에 접근 권한이 없습니다.", content = @Content)
})
public @interface SwaggerResponseAuth {
}
