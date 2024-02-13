package com.pgms.api.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class OAuthController {

	@GetMapping
	public String login() {
		return "login";
	}
}
