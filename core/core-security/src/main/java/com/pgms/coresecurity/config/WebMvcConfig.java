package com.pgms.coresecurity.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pgms.coresecurity.resolver.CurrentAccountArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final CurrentAccountArgumentResolver currentAccountArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentAccountArgumentResolver);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(getAllowOrigins())
			.allowedHeaders("Authorization", "Cache-Control", "Content-Type")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
			.allowCredentials(true);
	}

	private String[] getAllowOrigins() {
		return Arrays.asList(
			"http://127.0.0.1:3000",
			"http://localhost:3000",
			"http://127.0.0.1:5173",
			"http://localhost:5173"
		).toArray(String[]::new);
	}
}
