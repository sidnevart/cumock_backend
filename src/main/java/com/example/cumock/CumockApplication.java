package com.example.cumock;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class CumockApplication {

	public static void main(String[] args) {
		String key = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
		System.out.println("Key: " + key);
		SpringApplication.run(CumockApplication.class, args);
	}

}
