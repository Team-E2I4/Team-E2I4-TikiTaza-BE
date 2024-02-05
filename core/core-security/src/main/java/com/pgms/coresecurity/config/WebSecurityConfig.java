package com.pgms.coresecurity.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pgms.coresecurity.jwt.JwtAccessDeniedHandler;
import com.pgms.coresecurity.jwt.JwtAuthenticationEntryPoint;
import com.pgms.coresecurity.jwt.JwtAuthenticationFilter;
import com.pgms.coresecurity.service.OAuth2MemberService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final OAuth2MemberService oAuth2MemberService;
	private final AuthenticationSuccessHandler oauthSuccessHandler;

	/**
	 * 패스워드 관련 여러 인코딩 알고리즘 사용을 제공하는 DelegatingPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
			.requestMatchers("/h2-console/**");
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> filterRegistration(JwtAuthenticationFilter filter) {
		FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
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
					antMatcher("/login"),
					antMatcher("/login/oauth2/code/kakao"),
					antMatcher("/oauth2/authorization/kakao"),
					antMatcher("/oauth2/authorization/google"),
					antMatcher("/login/oauth2/code/google")
				))
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.permitAll())
			.oauth2Login(
				(oauth) ->
					oauth.userInfoEndpoint(
							(endpoint) -> endpoint.userService(oAuth2MemberService)
						)
						.successHandler(oauthSuccessHandler)
			);
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
			.addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				exception.accessDeniedHandler(jwtAccessDeniedHandler);
			});
		return http.build();
	}

	private RequestMatcher[] requestPermitAll() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/*/auth/**"),
			antMatcher("/api/*/members/sign-up")
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
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}
}
