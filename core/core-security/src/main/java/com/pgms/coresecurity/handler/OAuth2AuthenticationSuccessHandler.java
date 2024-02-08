package com.pgms.coresecurity.handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coreinfraredis.repository.RedisRepository;
import com.pgms.coresecurity.jwt.JwtTokenProvider;
import com.pgms.coresecurity.user.normal.UserDetailsImpl;
import com.pgms.coresecurity.user.oauth.CustomOAuth2User;
import com.pgms.coresecurity.util.HttpResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;
	private final RedisRepository redisRepository;

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Member member = customOAuth2User.getMember();

		List<GrantedAuthority> authorities = getAuthorities(member);
		UserDetailsImpl userDetails = UserDetailsImpl.builder()
			.id(member.getId())
			.email(member.getEmail())
			.authorities(authorities)
			.build();

		String accessToken = jwtTokenProvider.createAccessToken(userDetails);
		String refreshToken = jwtTokenProvider.createRefreshToken();

		redisRepository.save(accessToken, member.getId());

		Map<String, Object> body = Map.of(
			"accessToken", accessToken,
			"refreshToken", refreshToken
		);
		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, body);
	}

	private List<GrantedAuthority> getAuthorities(Member member) {
		return member.getRole() != null ?
			List.of(new SimpleGrantedAuthority(member.getRole().name()))
			: null;
	}
}
