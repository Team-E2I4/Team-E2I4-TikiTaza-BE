package com.pgms.api.domain.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation()
@ApiResponses({
	@ApiResponse(responseCode = "200", description = "성공")
})
public @interface SwaggerResponseMember {
}
