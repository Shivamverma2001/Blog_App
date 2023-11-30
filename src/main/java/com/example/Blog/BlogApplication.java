package com.example.Blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {
	public String PORT = System.getenv("PORT");

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

}
