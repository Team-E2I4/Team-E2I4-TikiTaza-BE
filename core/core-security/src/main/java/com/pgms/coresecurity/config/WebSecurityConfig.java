package com.pgms.coresecurity.config;

import static com.pgms.coredomain.domain.member.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pgms.coresecurity.jwt.JwtAccessDeniedHandler;
import com.pgms.coresecurity.jwt.JwtAuthenticationEntryPoint;
import com.pgms.coresecurity.jwt.JwtAuthenticationFilter;
import com.pgms.coresecurity.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 패스워드 관련 여러 인코딩 알고리즘 사용을 제공하는 DelegatingPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * permitAll 권한을 가진 엔드포인트에 적용되는 SecurityFilterChain
	 */
	@Bean
	public SecurityFilterChain securityFilterChainPermitAll(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http.securityMatchers(matchers -> matchers.requestMatchers(requestPermitAll()))
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.permitAll());
		return http.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChainOAuth(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.securityMatchers(matchers -> matchers
				.requestMatchers(
					antMatcher("/login/**"),
					antMatcher("/login/oauth2/code/**")
				))
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.permitAll());
		return http.build();
	}

	/**
	 * 인증 및 인가가 필요한 엔드포인트에 적용되는 SecurityFilterChain 입니다.
	 */
	@Bean
	public SecurityFilterChain securityFilterChainAuthorized(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.securityMatchers(matchers -> matchers
				.requestMatchers(requestHasRoleUser())
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(requestHasRoleUser()).hasAuthority(ROLE_USER.name())
				.anyRequest().authenticated() // 여기서 GUEST 권한도 처리
			)
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				exception.accessDeniedHandler(jwtAccessDeniedHandler);
			})
			.addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider), ExceptionTranslationFilter.class);
		return http.build();
	}

	/**
	 * 위에서 정의된 엔드포인트 이외에는 authenticated로 설정
	 */
	@Bean
	public SecurityFilterChain securityFilterChainDefault(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.authenticated()
			)
			.addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider), ExceptionTranslationFilter.class)
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				exception.accessDeniedHandler(jwtAccessDeniedHandler);
			});
		return http.build();
	}

	private RequestMatcher[] requestHasRoleUser() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher(POST, "/api/v1/auth/logout"),
			antMatcher(DELETE, "/api/v1/members"),
			antMatcher(PATCH, "/api/v1/members")
		);
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	private RequestMatcher[] requestPermitAll() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/"),
			antMatcher("/swagger-ui/**"),
			antMatcher("/v3/api-docs/**"),
			antMatcher("/ws/**"),
			antMatcher("/from/**"),
			antMatcher("/to/**"),
			antMatcher("/api/v1/auth/login/**"),
			antMatcher("/api/v1/auth/reissue"),
			antMatcher("/api/v1/auth/guest"),
			antMatcher("/api/v1/members/sign-up"),
			antMatcher("/api/v1/sse")
		);
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	private void configureCommonSecuritySettings(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.anonymous(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}
}
