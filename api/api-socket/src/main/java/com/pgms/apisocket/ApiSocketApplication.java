package com.pgms.apisocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pgms")
public class ApiSocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSocketApplication.class, args);
	}

}
