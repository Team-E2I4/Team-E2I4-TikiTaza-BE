package com.pgms.api.domain.online.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.online.dto.OnlineMemberGetResponse;
import com.pgms.api.domain.online.service.OnlineService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.resolver.Account;
import com.pgms.coresecurity.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "실시간 접속자", description = "실시간 접속자 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
public class OnlineController {

	private final OnlineService onlineService;

	@Operation(summary = "실시간 접속자 조회")
	@GetMapping("/api/v1/online")
	public ResponseEntity<ApiResponse<Set<OnlineMemberGetResponse>>> getOnlineMembers(
		@CurrentAccount Account account) {
		return ResponseEntity.ok(ApiResponse.of(onlineService.getOnlineMembers(account)));
	}
}
