package com.pgms.api.domain.online;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.online.dto.OnlineMemberGetResponse;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.resolver.Account;
import com.pgms.coresecurity.resolver.CurrentAccount;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OnlineController {

	private final OnlineService onlineService;

	@GetMapping("/api/v1/online")
	public ResponseEntity<ApiResponse<Set<OnlineMemberGetResponse>>> getOnlineMembers(
		@CurrentAccount Account account) {
		return ResponseEntity.ok(ApiResponse.of(onlineService.getOnlineMembers(account)));
	}
}
