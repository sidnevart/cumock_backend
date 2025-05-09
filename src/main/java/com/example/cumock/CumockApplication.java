package com.example.cumock;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class CumockApplication {

	@Value("${test.banner:NOT LOADED}")
	private String testBanner;

	@PostConstruct
	public void showBanner() {
		System.out.println(">>> " + testBanner);
	}
	public static void main(String[] args) {
		String key = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
		System.out.println("Key: " + key);
		SpringApplication.run(CumockApplication.class, args);
	}

}
