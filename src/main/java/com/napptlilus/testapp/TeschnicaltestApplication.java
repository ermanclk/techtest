package com.napptlilus.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
@EnableCaching
public class TeschnicaltestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeschnicaltestApplication.class, args);
	}

}
