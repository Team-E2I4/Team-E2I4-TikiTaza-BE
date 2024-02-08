package com.pgms.apigame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pgms"})
public class ApiGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGameApplication.class, args);
	}

}
