package com.pgms.apimember.controller;

import static com.pgms.coredomain.response.ResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.request.MemberSignUpRequest;
import com.pgms.apimember.dto.request.NicknameUpdateRequest;
import com.pgms.apimember.dto.response.MemberGetResponse;
import com.pgms.apimember.service.MemberService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.resolver.CurrentAccount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/sign-up")
	public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody MemberSignUpRequest request) {
		return ResponseEntity.ok(ApiResponse.of(memberService.signUp(request)));
	}

	@GetMapping("/my-profile")
	public ResponseEntity<ApiResponse<MemberGetResponse>> getMyProfileInfo(@CurrentAccount Long memberId) {
		return ResponseEntity.ok(ApiResponse.of(memberService.getMyProfileInfo(memberId)));
	}

	@PatchMapping
	public ResponseEntity<ApiResponse<Void>> updateMemberNickname(@CurrentAccount Long memberId,
		@RequestBody NicknameUpdateRequest request) {
		memberService.updateMemberNickname(memberId, request);
		return ResponseEntity.ok(ApiResponse.of(SUCCESS));
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteMemberAccount(@CurrentAccount Long memberId) {
		memberService.deleteMemberAccount(memberId);
		return ResponseEntity.ok(ApiResponse.of(SUCCESS));
	}
}
