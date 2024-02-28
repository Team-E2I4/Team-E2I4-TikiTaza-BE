package com.pgms.api.domain.member.service;

import static com.pgms.coredomain.exception.SecurityErrorCode.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.api.domain.member.dto.response.KakaoUserGetResponse;
import com.pgms.coresecurity.exception.SecurityException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String kakaoRedirectUri;

	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String kakaoClientSecret;

	public String getKakaoToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoClientId);
		params.add("client_secret", kakaoClientSecret);
		params.add("redirect_uri", kakaoRedirectUri);
		params.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(
			"https://kauth.kakao.com/oauth/token",
			kakaoTokenRequest,
			String.class
		);

		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.readTree(responseBody);
		} catch (JsonProcessingException e) {
			throw new SecurityException(OAUTH_LOGIN_FAILED);
		}
		return jsonNode.get("access_token").asText();
	}

	public KakaoUserGetResponse getKakaoUserInfo(String token) {
		HttpHeaders headers = new HttpHeaders();

		// HTTP 헤더 생성
		headers.add("Authorization", "Bearer " + token);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTP 요청 보내기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(headers);

		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity(
				"https://kapi.kakao.com/v2/user/me",
				kakaoTokenRequest,
				String.class
			);
			String responseBody = response.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseBody);
			String email = jsonNode.get("kakao_account").get("email").asText();
			String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText();
			return KakaoUserGetResponse.from(email, nickname);
		} catch (HttpClientErrorException e) {
			throw new SecurityException(INVALID_OAUTH_CODE);
		} catch (JsonProcessingException e) {
			throw new SecurityException(OAUTH_LOGIN_FAILED);
		}
	}
}
