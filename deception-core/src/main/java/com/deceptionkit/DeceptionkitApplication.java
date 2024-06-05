package com.deceptionkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DeceptionkitApplication {

	public static void main(String[] args) {
		SpringApplication springApp = new SpringApplication(DeceptionkitApplication.class);
		springApp.run(args);
	}

}