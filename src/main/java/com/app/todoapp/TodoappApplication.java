package com.app.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TodoappApplication {
//http://localhost:8081/todotask/swagger-ui/#/
	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);
	}
	
	
}
