package com.pgms.api.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.api.domain.member.dto.request.MemberSignUpRequest;
import com.pgms.api.domain.member.dto.request.NicknameUpdateRequest;
import com.pgms.api.domain.member.dto.response.MemberGetResponse;
import com.pgms.api.domain.member.service.MemberService;
import com.pgms.api.global.annotation.SwaggerResponseMember;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coredomain.response.ResponseCode;
import com.pgms.coresecurity.jwt.JwtTokenProvider;
import com.pgms.coresecurity.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원", description = "회원 관련 API 입니다.")
@SwaggerResponseMember
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;

	@Operation(summary = "회원가입")
	@PostMapping("/sign-up")
	public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody MemberSignUpRequest request) {
		return ResponseEntity.ok(ApiResponse.of(memberService.signUp(request)));
	}

	@Operation(summary = "내 프로필 조회")
	@GetMapping("/my-profile")
	public ResponseEntity<ApiResponse<MemberGetResponse>> getMyProfileInfo(@CurrentAccount Long memberId) {
		return ResponseEntity.ok(ApiResponse.of(memberService.getMyProfileInfo(memberId)));
	}

	@Operation(summary = "회원 닉네임 업데이트. 소셜 로그인 시 사용")
	@PatchMapping
	public ResponseEntity<ApiResponse<Void>> updateMemberNickname(@CurrentAccount Long memberId,
		@RequestBody @Valid NicknameUpdateRequest request) {
		memberService.updateMemberNickname(memberId, request);
		return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS));
	}

	@Operation(summary = "회원 삭제")
	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteMemberAccount(
		@RequestHeader("Authorization") String bearerToken,
		@CookieValue("refreshToken") String refreshToken,
		@CurrentAccount Long memberId) {
		memberService.deleteMemberAccount(jwtTokenProvider.resolveToken(bearerToken), refreshToken, memberId);
		return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS));
	}
}
