package com.pgms.apimember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pgms"})
public class ApiMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMemberApplication.class, args);
	}
}
