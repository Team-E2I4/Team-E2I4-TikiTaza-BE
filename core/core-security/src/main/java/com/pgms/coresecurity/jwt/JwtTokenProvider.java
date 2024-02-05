package com.pgms.coresecurity.jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.pgms.coresecurity.user.normal.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final String AUTHENTICATION_CLAIM_NAME = "roles";

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.access-expiry-seconds}")
	private int accessExpirySeconds;

	@Value("${jwt.refresh-expiry-seconds}")
	private int refreshExpirySeconds;

	public String createAccessToken(UserDetailsImpl userDetails) {
		Instant now = Instant.now();
		Instant expirationTime = now.plusSeconds(accessExpirySeconds);

		String authorities = null;
		if (userDetails.getAuthorities() != null) {
			authorities = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		}

		return Jwts.builder()
			.claim("id", userDetails.getId())
			.subject((userDetails.getUsername()))
			.issuedAt(Date.from(now))
			.expiration(Date.from(expirationTime))
			.claim(AUTHENTICATION_CLAIM_NAME, authorities)
			.signWith(extractSecretKey())
			.compact();
	}

	public String createRefreshToken() {
		Instant now = Instant.now();
		Instant expirationTime = now.plusSeconds(refreshExpirySeconds);

		return Jwts.builder()
			.issuedAt(Date.from(now))
			.expiration(Date.from(expirationTime))
			.signWith(extractSecretKey())
			.compact();
	}

	/**
	 * 권한 체크
	 */
	public Authentication getAuthentication(String accessToken) {
		Claims claims = verifyAndExtractClaims(accessToken);

		Collection<? extends GrantedAuthority> authorities = null;
		if (claims.get(AUTHENTICATION_CLAIM_NAME) != null) {
			authorities = Arrays.stream(claims.get(AUTHENTICATION_CLAIM_NAME)
					.toString()
					.split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();
		}

		UserDetailsImpl principal = UserDetailsImpl.builder()
			.id(claims.get("id", Long.class))
			.email(claims.getSubject())
			.password(null)
			.authorities(authorities)
			.build();

		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	/**
	 * Jwt 검증 및 클레임 추출
	 */
	private Claims verifyAndExtractClaims(String accessToken) {
		return Jwts.parser()
			.verifyWith(extractSecretKey())
			.build()
			.parseSignedClaims(accessToken)
			.getPayload();
	}

	public void validateAccessToken(String accessToken) {
		Jwts.parser()
			.verifyWith(extractSecretKey())
			.build()
			.parse(accessToken);
	}

	/**
	 * SecretKey 추출
	 */
	private SecretKey extractSecretKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
}
